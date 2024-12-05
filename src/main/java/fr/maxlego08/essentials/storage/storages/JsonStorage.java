package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.dto.CooldownDTO;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.EconomyTransactionDTO;
import fr.maxlego08.essentials.api.dto.HomeDTO;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.dto.PlayerSlotDTO;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.dto.UserDTO;
import fr.maxlego08.essentials.api.dto.UserEconomyDTO;
import fr.maxlego08.essentials.api.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.dto.UserVoteDTO;
import fr.maxlego08.essentials.api.dto.VaultDTO;
import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.user.UserRecord;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        this.plugin.getLogger().severe("Please use MYSQL storage, the JSON is only to enable the for the first installation of the plugin.");
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
    public void deleteCooldown(UUID uniqueId, String key) {
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
    }

    @Override
    public void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount, String reason) {

    }

    @Override
    public List<EconomyTransactionDTO> getTransactions(UUID toUuid, Economy economy) {
        return List.of();
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
        // throw new NotImplementedException("insertChatMessage is not implemented, use MYSQL storage");
    }

    @Override
    public void insertCommand(UUID uuid, String command) {
        // throw new NotImplementedException("insertCommand is not implemented, use MYSQL storage");
    }

    @Override
    public void insertPlayTime(UUID uniqueId, long sessionPlayTime, long playtime, String address) {
        // throw new NotImplementedException("insertPlayTime is not implemented, use MYSQL storage");
    }

    @Override
    public UserRecord fetchUserRecord(UUID uuid) {
        throw new NotImplementedException("UserRecord is not implemented, use MYSQL storage");
    }

    @Override
    public List<UserDTO> getUsers(String ip) {
        throw new NotImplementedException("getUsers is not implemented, use MYSQL storage");
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

    @Override
    public List<CooldownDTO> getCooldowns(UUID uniqueId) {
        return new ArrayList<>();
    }

    @Override
    public void setPowerTools(UUID uniqueId, Material type, String command) {
        this.saveFileAsync(uniqueId);
    }

    @Override
    public void deletePowerTools(UUID uniqueId, Material material) {
        this.saveFileAsync(uniqueId);
    }

    @Override
    public void addMailBoxItem(MailBoxItem mailBoxItem) {
        throw new NotImplementedException("addMailBoxItem is not implemented, use MYSQL storage");
    }

    @Override
    public void removeMailBoxItem(int id) {
        throw new NotImplementedException("removeMailBoxItem is not implemented, use MYSQL storage");
    }

    @Override
    public List<UserEconomyRankingDTO> getEconomyRanking(Economy economy) {
        return new ArrayList<>();
    }

    @Override
    public List<MailBoxDTO> getMailBox(UUID uuid) {
        return new ArrayList<>();
    }

    @Override
    public void fetchOfflinePlayerEconomies(Consumer<List<UserEconomyDTO>> consumer) {
        consumer.accept(new ArrayList<>());
    }

    @Override
    public void setVote(UUID uuid, long vote, long offline) {
        throw new NotImplementedException("setVote is not implemented, use MYSQL storage");
    }

    @Override
    public UserVoteDTO getVote(UUID uniqueId) {
        throw new NotImplementedException("getVote is not implemented, use MYSQL storage");
    }

    @Override
    public void updateServerStorage(String key, Object object) {
        throw new NotImplementedException("updateServerStorage is not implemented, use MYSQL storage");
    }

    @Override
    public void setLastVote(UUID uniqueId, String site) {
        throw new NotImplementedException("setLastVote is not implemented, use MYSQL storage");
    }

    @Override
    public void resetVotes() {
        throw new NotImplementedException("resetVotes is not implemented, use MYSQL storage");
    }

    @Override
    public void updateVaultQuantity(UUID uniqueId, int vaultId, int slot, long quantity) {
        throw new NotImplementedException("updateVaultQuantity is not implemented, use MYSQL storage");
    }

    @Override
    public void removeVaultItem(UUID uniqueId, int vaultId, int slot) {
        throw new NotImplementedException("removeVaultItem is not implemented, use MYSQL storage");
    }

    @Override
    public void createVaultItem(UUID uniqueId, int vaultId, int slot, long quantity, String item) {
        throw new NotImplementedException("createVaultItem is not implemented, use MYSQL storage");

    }

    @Override
    public void setVaultSlot(UUID uniqueId, int slots) {
        throw new NotImplementedException("setVaultSlot is not implemented, use MYSQL storage");
    }

    @Override
    public List<VaultDTO> getVaults() {
        return new ArrayList<>();
    }

    @Override
    public List<VaultItemDTO> getVaultItems() {
        return new ArrayList<>();
    }

    @Override
    public List<PlayerSlotDTO> getPlayerVaultSlots() {
        return new ArrayList<>();
    }

    @Override
    public void updateVault(UUID uniqueId, Vault vault) {
        throw new NotImplementedException("updateVault is not implemented, use MYSQL storage");
    }

    @Override
    public void updateUserFrozen(UUID uuid, boolean frozen) {
        this.saveFileAsync(uuid);
    }

    @Override
    public void upsertFlySeconds(UUID uniqueId, long flySeconds) {
        throw new NotImplementedException("upsertFly is not implemented, use MYSQL storage");
    }

    @Override
    public long getFlySeconds(UUID uniqueId) {
        return 0;
    }

    @Override
    public void deleteWorldData(String worldName) {
        throw new NotImplementedException("upsertFly is not implemented, use MYSQL storage");
    }
}
