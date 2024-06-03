package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.database.dto.CommandDTO;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.database.dto.PlayTimeDTO;
import fr.maxlego08.essentials.api.database.dto.PowerToolsDTO;
import fr.maxlego08.essentials.api.database.dto.SanctionDTO;
import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.api.database.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.user.UserRecord;
import fr.maxlego08.essentials.storage.database.Repositories;
import fr.maxlego08.essentials.storage.database.repositeries.ChatMessagesRepository;
import fr.maxlego08.essentials.storage.database.repositeries.CommandsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.EconomyTransactionsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserCooldownsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserMailBoxRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserOptionRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserPlayTimeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserPowerToolsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserSanctionRepository;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.MySqlConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SqlStorage extends StorageHelper implements IStorage {

    private final DatabaseConnection connection;
    private final Repositories repositories;

    public SqlStorage(EssentialsPlugin plugin) {
        super(plugin);
        this.connection = new MySqlConnection(plugin.getConfiguration().getDatabaseConfiguration());

        if (!this.connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database !");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            plugin.getLogger().info("The database connection is valid ! (" + connection.getDatabaseConfiguration().host() + ")");
        }

        this.repositories = new Repositories(plugin, this.connection);
        this.repositories.register(UserRepository.class);
        this.repositories.register(UserOptionRepository.class);
        this.repositories.register(UserCooldownsRepository.class);
        this.repositories.register(UserEconomyRepository.class);
        this.repositories.register(EconomyTransactionsRepository.class);
        this.repositories.register(UserHomeRepository.class);
        this.repositories.register(UserSanctionRepository.class);
        this.repositories.register(ChatMessagesRepository.class);
        this.repositories.register(CommandsRepository.class);
        this.repositories.register(UserPlayTimeRepository.class);
        this.repositories.register(UserPowerToolsRepository.class);
        this.repositories.register(UserMailBoxRepository.class);
        // this.repositories.register(ServerStorageRepository.class);

        MigrationManager.execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.plugin.getLogger());

        this.repositories.getTable(UserCooldownsRepository.class).deleteExpiredCooldowns();
        this.repositories.getTable(UserRepository.class).clearExpiredSanctions();
        this.repositories.getTable(UserMailBoxRepository.class).deleteExpiredItems();
        this.setActiveSanctions(this.repositories.getTable(UserSanctionRepository.class).getActiveBan());

        /*List<ServerStorageDTO> serverStorageDTOS = this.repositories.getTable(ServerStorageRepository.class).select();
        plugin.getServerStorage().setContents(serverStorageDTOS);*/
    }

    @Override
    public void onEnable() {

        this.connection.connect();
        this.totalUser = this.repositories.getTable(UserRepository.class).totalUsers();
    }

    @Override
    public void onDisable() {

        this.connection.disconnect();
    }

    @Override
    public User createOrLoad(UUID uniqueId, String playerName) {

        User user = new ZUser(this.plugin, uniqueId);
        user.setName(playerName);
        this.users.put(uniqueId, user);

        this.plugin.getScheduler().runAsync(wrappedTask -> {

            List<UserDTO> userDTOS = this.repositories.getTable(UserRepository.class).selectUser(uniqueId);
            // First join !
            if (userDTOS.isEmpty()) {
                this.firstJoin(user);
            }

            this.repositories.getTable(UserRepository.class).upsert(uniqueId, playerName); // Create the player or update his name
            if (!userDTOS.isEmpty()) {

                UserDTO userDTO = userDTOS.get(0);

                if (userDTO.mute_sanction_id() != null) { // Check if player is mute
                    SanctionDTO sanction = this.repositories.getTable(UserSanctionRepository.class).getSanction(userDTO.mute_sanction_id());
                    if (sanction.isActive()) {
                        user.setMuteSanction(Sanction.fromDTO(sanction));
                    }
                }

                user.setSanction(userDTO.ban_sanction_id(), userDTO.mute_sanction_id());
                user.setLastLocation(stringAsLocation(userDTO.last_location()));
                user.setPlayTime(userDTO.play_time());
                user.setOptions(this.repositories.getTable(UserOptionRepository.class).selectOptions(uniqueId));
                user.setCooldowns(this.repositories.getTable(UserCooldownsRepository.class).selectCooldowns(uniqueId));
                user.setEconomies(this.repositories.getTable(UserEconomyRepository.class).selectEconomies(uniqueId));
                user.setHomes(this.repositories.getTable(UserHomeRepository.class).selectHomes(uniqueId));
                user.setPowerTools(this.repositories.getTable(UserPowerToolsRepository.class).getPowerTools(uniqueId).stream().collect(Collectors.toMap(PowerToolsDTO::material, PowerToolsDTO::command)));
                user.setMailBoxItems(this.repositories.getTable(UserMailBoxRepository.class).select(uniqueId));
            }
        });

        return user;
    }

    @Override
    public void onPlayerQuit(UUID uniqueId) {
        this.users.remove(uniqueId);
    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.users.get(uniqueId);
    }

    @Override
    public void updateOption(UUID uniqueId, Option option, boolean value) {
        async(() -> this.repositories.getTable(UserOptionRepository.class).upsert(uniqueId, option, value));
    }

    @Override
    public void updateCooldown(UUID uniqueId, String key, long expiredAt) {
        async(() -> this.repositories.getTable(UserCooldownsRepository.class).upsert(uniqueId, key, expiredAt));
    }

    @Override
    public void deleteCooldown(UUID uniqueId, String key) {
        async(() -> this.repositories.getTable(UserCooldownsRepository.class).delete(uniqueId, key));
    }

    @Override
    public void updateEconomy(UUID uniqueId, Economy economy, BigDecimal bigDecimal) {
        async(() -> this.repositories.getTable(UserEconomyRepository.class).upsert(uniqueId, economy, bigDecimal));
    }

    @Override
    public void upsertUser(User user) {
        async(() -> this.repositories.getTable(UserRepository.class).upsert(user));
    }

    @Override
    public void updateUserMoney(UUID uniqueId, Consumer<User> consumer) {
        User fakeUser = new ZUser(this.plugin, uniqueId);
        consumer.accept(fakeUser);
    }

    @Override
    public void getUserEconomy(String userName, Consumer<List<EconomyDTO>> consumer) {
        async(() -> {
            List<EconomyDTO> economyDTOS = getLocalEconomyDTO(userName);
            if (!economyDTOS.isEmpty()) {
                consumer.accept(economyDTOS);
                return;
            }

            fetchUniqueId(userName, uuid -> {
                if (uuid == null) {
                    consumer.accept(new ArrayList<>());
                    return;
                }

                consumer.accept(this.repositories.getTable(UserEconomyRepository.class).selectEconomies(uuid));
            });
        });
    }

    @Override
    public void fetchUniqueId(String userName, Consumer<UUID> consumer) {

        if (this.localUUIDS.containsKey(userName)) {
            consumer.accept(this.localUUIDS.get(userName));
            return;
        }

        // User server cache
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(userName);
        if (offlinePlayer != null) {
            this.localUUIDS.put(userName, offlinePlayer.getUniqueId());
            consumer.accept(offlinePlayer.getUniqueId());
            return;
        }

        async(() -> {
            // User plugin cache first
            getLocalUniqueId(userName).ifPresentOrElse(uuid -> {
                this.localUUIDS.put(userName, uuid);
                consumer.accept(uuid);
            }, () -> {

                // Get uuid from database
                List<UserDTO> userDTOS = this.repositories.getTable(UserRepository.class).selectUsers(userName);
                if (userDTOS.isEmpty()) {
                    consumer.accept(null);
                    return;
                }

                UserDTO userDTO = userDTOS.get(0);
                this.localUUIDS.put(userName, userDTO.unique_id());
                consumer.accept(userDTO.unique_id());
            });
        });
    }

    @Override
    public void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount) {
        async(() -> this.repositories.getTable(EconomyTransactionsRepository.class).upsert(fromUuid, toUuid, economy, fromAmount, toAmount));
    }

    @Override
    public void upsertStorage(String key, Object value) {
        // async(() -> this.repositories.getTable(ServerStorageRepository.class).upsert(key, value));
    }

    @Override
    public void upsertHome(UUID uniqueId, Home home) {
        async(() -> this.repositories.getTable(UserHomeRepository.class).upsert(uniqueId, home));
    }

    @Override
    public void deleteHome(UUID uniqueId, String name) {
        async(() -> this.repositories.getTable(UserHomeRepository.class).deleteHomes(uniqueId, name));
    }

    @Override
    public CompletableFuture<List<Home>> getHome(UUID uuid, String homeName) {
        CompletableFuture<List<Home>> future = new CompletableFuture<>();
        future.complete(this.repositories.getTable(UserHomeRepository.class).getHomes(uuid, homeName));
        return future;
    }

    @Override
    public CompletionStage<List<Home>> getHomes(UUID uuid) {
        CompletableFuture<List<Home>> future = new CompletableFuture<>();
        future.complete(this.repositories.getTable(UserHomeRepository.class).getHomes(uuid));
        return future;
    }

    @Override
    public void insertSanction(Sanction sanction, Consumer<Integer> consumer) {
        if (sanction.getSanctionType() == SanctionType.BAN) {
            this.banSanctions.put(sanction.getPlayerUniqueId(), sanction);
        } else if (sanction.getSanctionType() == SanctionType.UNBAN) {
            this.banSanctions.remove(sanction.getPlayerUniqueId());
        }

        async(() -> this.repositories.getTable(UserSanctionRepository.class).insert(sanction, consumer));
    }

    @Override
    public void updateUserBan(UUID uuid, Integer index) {
        if (index == null) this.banSanctions.remove(uuid);
        async(() -> this.repositories.getTable(UserRepository.class).updateBanId(uuid, index));
    }

    @Override
    public void updateUserMute(UUID uuid, Integer index) {
        async(() -> this.repositories.getTable(UserRepository.class).updateMuteId(uuid, index));
    }

    @Override
    public boolean isMute(UUID uuid) {
        Sanction sanction = getMute(uuid);
        return sanction != null && sanction.isActive();
    }

    @Override
    public Sanction getMute(UUID uuid) {
        List<UserDTO> userDTOS = this.repositories.getTable(UserRepository.class).selectUser(uuid);
        if (userDTOS.isEmpty()) return null;

        UserDTO userDTO = userDTOS.get(0);

        if (userDTO.mute_sanction_id() != null) {
            SanctionDTO sanction = this.repositories.getTable(UserSanctionRepository.class).getSanction(userDTO.mute_sanction_id());
            return Sanction.fromDTO(sanction);
        }
        return null;
    }

    @Override
    public List<SanctionDTO> getSanctions(UUID uuid) {
        return this.repositories.getTable(UserSanctionRepository.class).getSanctions(uuid);
    }

    @Override
    public void insertChatMessage(UUID uuid, String content) {
        async(() -> this.repositories.getTable(ChatMessagesRepository.class).insert(new ChatMessageDTO(uuid, content, new Date())));
    }

    @Override
    public void insertCommand(UUID uuid, String command) {
        async(() -> this.repositories.getTable(CommandsRepository.class).insert(new CommandDTO(uuid, command, new Date())));
    }

    @Override
    public void insertPlayTime(UUID uniqueId, long sessionPlayTime, long playtime, String address) {
        async(() -> {
            if (sessionPlayTime > 0) {
                this.repositories.getTable(UserPlayTimeRepository.class).insert(uniqueId, sessionPlayTime, address);
            }
            this.repositories.getTable(UserRepository.class).updatePlayTime(uniqueId, playtime);
        });
    }

    @Override
    public List<UserDTO> getUsers(String ip) {
        return this.repositories.getTable(UserRepository.class).getUsers(ip);
    }

    @Override
    public UserRecord fetchUserRecord(UUID uuid) {

        UserDTO userDTO = this.repositories.getTable(UserRepository.class).selectUser(uuid).get(0);
        List<PlayTimeDTO> playTimeDTOS = this.repositories.getTable(UserPlayTimeRepository.class).select(uuid);

        return new UserRecord(userDTO, playTimeDTOS);
    }

    @Override
    public List<ChatMessageDTO> getMessages(UUID targetUuid) {
        return this.repositories.getTable(ChatMessagesRepository.class).getMessages(targetUuid);
    }

    @Override
    public Map<Option, Boolean> getOptions(UUID uuid) {
        if (this.users.containsKey(uuid)) {
            return this.users.get(uuid).getOptions();
        }
        return this.repositories.getTable(UserOptionRepository.class).selectOptions(uuid).stream().collect(Collectors.toMap(OptionDTO::option_name, OptionDTO::option_value));
    }

    @Override
    public List<CooldownDTO> getCooldowns(UUID uniqueId) {
        return this.repositories.getTable(UserCooldownsRepository.class).selectCooldowns(uniqueId);
    }

    @Override
    public void setPowerTools(UUID uniqueId, Material material, String command) {
        async(() -> this.repositories.getTable(UserPowerToolsRepository.class).upsert(uniqueId, material, command));
    }

    @Override
    public void deletePowerTools(UUID uniqueId, Material material) {
        async(() -> this.repositories.getTable(UserPowerToolsRepository.class).delete(uniqueId, material));
    }

    @Override
    public void addMailBoxItem(MailBoxItem mailBoxItem) {
        async(() -> this.repositories.getTable(UserMailBoxRepository.class).insert(mailBoxItem));
    }

    @Override
    public void removeMailBoxItem(int id) {
        async(() -> this.repositories.getTable(UserMailBoxRepository.class).delete(id));
    }

    @Override
    public List<UserEconomyRankingDTO> getEconomyRanking(Economy economy) {
        return this.repositories.getTable(UserRepository.class).getBalanceRanking(economy.getName());
    }
}
