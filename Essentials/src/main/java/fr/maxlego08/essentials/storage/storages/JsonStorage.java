package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.HomeDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.database.dto.SanctionDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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

        File folder = getFolder();
        this.totalUser = folder == null ? 0 : Optional.ofNullable(folder.listFiles()).map(e -> e.length).orElse(0);
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
            this.firstJoin(user);

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
    public void upsertUser(User user) {
        this.saveFileAsync(user.getUniqueId());
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

    @Override
    public void upsertStorage(String key, Object value) {

    }

    @Override
    public void upsertHome(UUID uniqueId, Home home) {
        User user = getUser(uniqueId);
        if (user == null) {
            user = createOrLoad(uniqueId, "");
            user.setHomes(List.of(new HomeDTO(locationAsString(home.getLocation()), home.getName(), home.getMaterial() == null ? null : home.getMaterial().name())));
            User finalUser = user;
            this.plugin.getScheduler().runAsync(wrappedTask -> {
                Persist persist = this.plugin.getPersist();
                persist.save(finalUser, getUserFile(uniqueId));
            });
            return;
        }
        this.saveFileAsync(uniqueId);
    }

    @Override
    public void deleteHome(UUID uniqueId, String name) {
        User user = getUser(uniqueId);
        if (user == null) {
            user = createOrLoad(uniqueId, "");
            user.removeHome(name);
            User finalUser = user;
            this.plugin.getScheduler().runAsync(wrappedTask -> {
                Persist persist = this.plugin.getPersist();
                persist.save(finalUser, getUserFile(uniqueId));
            });
            return;
        }
        this.saveFileAsync(uniqueId);
    }

    @Override
    public CompletableFuture<List<Home>> getHome(UUID uuid, String homeName) {
        CompletableFuture<List<Home>> future = new CompletableFuture<>();
        future.complete(createOrLoad(uuid, "").getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(homeName)).toList());
        return future;
    }

    @Override
    public CompletionStage<List<Home>> getHomes(UUID uuid) {
        CompletableFuture<List<Home>> future = new CompletableFuture<>();
        future.complete(createOrLoad(uuid, "").getHomes());
        return future;
    }

    @Override
    public void insertSanction(Sanction sanction, Consumer<Integer> consumer) {
        throw new NotImplementedException("insertSanction is not implemented, use MYSQL storage");
    }

    @Override
    public void updateUserBan(UUID uuid, Integer index) {
        throw new NotImplementedException("updateUserBan is not implemented, use MYSQL storage");
    }

    @Override
    public void updateUserMute(UUID uuid, Integer index) {
        throw new NotImplementedException("updateMuteBan is not implemented, use MYSQL storage");
    }

    @Override
    public boolean isMute(UUID uuid) {
        throw new NotImplementedException("isMute is not implemented, use MYSQL storage");
    }

    @Override
    public Sanction getMute(UUID uuid) {
        throw new NotImplementedException("getMute is not implemented, use MYSQL storage");
    }

    @Override
    public List<SanctionDTO> getSanctions(UUID uuid) {
        throw new NotImplementedException("getSanctions is not implemented, use MYSQL storage");
    }

    @Override
    public void insertChatMessage(UUID uuid, String content) {
        throw new NotImplementedException("insertChatMessage is not implemented, use MYSQL storage");
    }

    @Override
    public List<ChatMessageDTO> getMessages(UUID targetUuid) {
        return new ArrayList<>();
    }

    @Override
    public Map<Option, Boolean> getOptions(UUID uuid) {
        if (this.users.containsKey(uuid)) {
            return this.users.get(uuid).getOptions();
        }
        return new HashMap<>();
    }
}
