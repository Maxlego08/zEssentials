package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.event.events.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class JsonStorage extends StorageHelper implements IStorage {

    public JsonStorage(EssentialsPlugin plugin) {
        super(plugin);
    }

    public File getFolder() {
        return new File(this.plugin.getDataFolder(), "users");
    }

    private void createFolder() {
        File folder = getFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onEnable() {
        this.createFolder();
    }

    @Override
    public void onDisable() {
        this.createFolder();
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uniqueId = player.getUniqueId();
            User user = getUser(uniqueId);
            Persist persist = this.plugin.getPersist();
            persist.save(user, getUserFile(uniqueId));
        });
    }

    private File getUserFile(UUID uniqueId) {
        return new File(getFolder(), uniqueId + ".json");
    }

    @Override
    public User createOrLoad(UUID uniqueId, String playerName) {

        this.createFolder();

        File file = getUserFile(uniqueId);
        Persist persist = this.plugin.getPersist();
        User user = persist.load(User.class, file);

        // If user is null, we need to create a new user
        if (user == null) {

            user = new ZUser(plugin, uniqueId);
            user.setName(playerName);

            this.plugin.getLogger().info(String.format("%s (%s) is a new player !", playerName, uniqueId));
            UserEvent event = new UserFirstJoinEvent(user);
            this.plugin.getScheduler().runNextTick(wrappedTask -> event.callEvent());

            persist.save(user, file);
        }

        this.users.put(uniqueId, user);
        return user;
    }

    private void saveFileAsync(UUID uniqueId) {
        User user = getUser(uniqueId);
        if (user == null) return;
        this.plugin.getScheduler().runAsync(wrappedTask -> {
            Persist persist = this.plugin.getPersist();
            persist.save(user, getUserFile(uniqueId));
        });
    }

    @Override
    public void onPlayerQuit(UUID uniqueId) {
        this.saveFileAsync(uniqueId);
        this.users.remove(uniqueId);
    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.users.get(uniqueId);
    }

    @Override
    public void updateOption(UUID uniqueId, Option option, boolean value) {
        this.saveFileAsync(uniqueId);
    }

    @Override
    public void updateCooldown(UUID uniqueId, String key, long expiredAt) {
        this.saveFileAsync(uniqueId);
    }

    @Override
    public void updateEconomy(UUID uniqueId, Economy economy, BigDecimal bigDecimal) {
        this.saveFileAsync(uniqueId);
    }

    @Override
    public void updateUserMoney(UUID uniqueId, Consumer<User> consumer) {
        User loadUser = createOrLoad(uniqueId, "offline");
        consumer.accept(loadUser);
    }

    @Override
    public void getUserEconomy(String userName, Consumer<List<EconomyDTO>> consumer) {
        async(() -> {

            List<EconomyDTO> economyDTOS = getLocalEconomyDTO(userName);
            if (!economyDTOS.isEmpty()) {
                consumer.accept(economyDTOS);
                return;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userName);
            User loadUser = createOrLoad(offlinePlayer.getUniqueId(), "offline");
            consumer.accept(loadUser.getBalances().entrySet().stream().map(e -> new EconomyDTO(e.getKey(), e.getValue())).toList());
        });
    }

    @Override
    public void fetchUniqueId(String userName, Consumer<UUID> consumer) {

        if (this.localUUIDS.containsKey(userName)) {
            consumer.accept(this.localUUIDS.get(userName));
            return;
        }

        async(() -> {
            // User plugin cache first
            getLocalUniqueId(userName).ifPresentOrElse(uuid -> {
                this.localUUIDS.put(userName, uuid);
                consumer.accept(uuid);
            }, () -> {
                // User server cache
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(userName);
                if (offlinePlayer != null) {
                    this.localUUIDS.put(userName, offlinePlayer.getUniqueId());
                    consumer.accept(offlinePlayer.getUniqueId());
                    return;
                }
                // Try load offline player
                offlinePlayer = Bukkit.getOfflinePlayer(userName);
                this.localUUIDS.put(userName, offlinePlayer.getUniqueId());
                consumer.accept(offlinePlayer.getUniqueId());
            });
        });
    }

    @Override
    public void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount) {

    }
}
