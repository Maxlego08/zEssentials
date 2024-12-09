package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.dto.CommandDTO;
import fr.maxlego08.essentials.api.dto.CooldownDTO;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.EconomyTransactionDTO;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.dto.OptionDTO;
import fr.maxlego08.essentials.api.dto.PlayTimeDTO;
import fr.maxlego08.essentials.api.dto.PlayerSlotDTO;
import fr.maxlego08.essentials.api.dto.PowerToolsDTO;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.dto.ServerStorageDTO;
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
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.user.UserRecord;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.migrations.CreateChatMessageMigration;
import fr.maxlego08.essentials.migrations.CreateCommandsMigration;
import fr.maxlego08.essentials.migrations.CreateEconomyTransactionMigration;
import fr.maxlego08.essentials.migrations.CreatePlayerSlots;
import fr.maxlego08.essentials.migrations.CreatePlayerVault;
import fr.maxlego08.essentials.migrations.CreatePlayerVaultItem;
import fr.maxlego08.essentials.migrations.CreateSanctionsTableMigration;
import fr.maxlego08.essentials.migrations.CreateServerStorageTableMigration;
import fr.maxlego08.essentials.migrations.CreateUserCooldownTableMigration;
import fr.maxlego08.essentials.migrations.CreateUserEconomyMigration;
import fr.maxlego08.essentials.migrations.CreateUserHomeTableMigration;
import fr.maxlego08.essentials.migrations.CreateUserMailBoxMigration;
import fr.maxlego08.essentials.migrations.CreateUserOptionTableMigration;
import fr.maxlego08.essentials.migrations.CreateUserPlayTimeTableMigration;
import fr.maxlego08.essentials.migrations.CreateUserPowerToolsMigration;
import fr.maxlego08.essentials.migrations.CreateUserTableMigration;
import fr.maxlego08.essentials.migrations.CreateVoteSiteMigration;
import fr.maxlego08.essentials.migrations.UpdateEconomyTransactionAddColumn;
import fr.maxlego08.essentials.migrations.UpdateUserTableAddFlyColumn;
import fr.maxlego08.essentials.migrations.UpdateUserTableAddFreezeColumn;
import fr.maxlego08.essentials.migrations.UpdateUserTableAddSanctionColumns;
import fr.maxlego08.essentials.migrations.UpdateUserTableAddVoteColumn;
import fr.maxlego08.essentials.storage.database.Repositories;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.repositeries.ChatMessagesRepository;
import fr.maxlego08.essentials.storage.database.repositeries.CommandsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.EconomyTransactionsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.PlayerSlotRepository;
import fr.maxlego08.essentials.storage.database.repositeries.ServerStorageRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserCooldownsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserMailBoxRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserOptionRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserPlayTimeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserPowerToolsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserSanctionRepository;
import fr.maxlego08.essentials.storage.database.repositeries.VaultItemRepository;
import fr.maxlego08.essentials.storage.database.repositeries.VaultRepository;
import fr.maxlego08.essentials.storage.database.repositeries.VoteSiteRepository;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.MySqlConnection;
import fr.maxlego08.sarah.SqliteConnection;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.logger.JULogger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

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

    public SqlStorage(EssentialsPlugin plugin, StorageType storageType) {
        super(plugin);
        DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(plugin, storageType);
        this.connection = switch (storageType) {
            case HIKARICP -> new HikariDatabaseConnection(databaseConfiguration);
            case SQLITE -> new SqliteConnection(databaseConfiguration, plugin.getDataFolder());
            default -> new MySqlConnection(databaseConfiguration);
        };

        if (!this.connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database !");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            if (storageType == StorageType.SQLITE) {
                plugin.getLogger().info("The database connection is valid ! (SQLITE)");
            } else {
                plugin.getLogger().info("The database connection is valid ! (" + connection.getDatabaseConfiguration().getHost() + ")");
            }
        }

        // Migrations
        MigrationManager.setMigrationTableName("zessentials_migrations");
        MigrationManager.setDatabaseConfiguration(databaseConfiguration);

        MigrationManager.registerMigration(new CreateUserTableMigration());
        MigrationManager.registerMigration(new CreateServerStorageTableMigration());
        MigrationManager.registerMigration(new CreateUserOptionTableMigration());
        MigrationManager.registerMigration(new CreateUserCooldownTableMigration());
        MigrationManager.registerMigration(new CreateUserEconomyMigration());
        MigrationManager.registerMigration(new CreateEconomyTransactionMigration());
        MigrationManager.registerMigration(new CreateUserHomeTableMigration());
        MigrationManager.registerMigration(new CreateSanctionsTableMigration());
        MigrationManager.registerMigration(new UpdateUserTableAddSanctionColumns());
        MigrationManager.registerMigration(new CreateChatMessageMigration());
        MigrationManager.registerMigration(new CreateCommandsMigration());
        MigrationManager.registerMigration(new CreateUserPlayTimeTableMigration());
        MigrationManager.registerMigration(new CreateUserPowerToolsMigration());
        MigrationManager.registerMigration(new CreateUserMailBoxMigration());
        MigrationManager.registerMigration(new UpdateUserTableAddVoteColumn());
        MigrationManager.registerMigration(new CreateVoteSiteMigration());
        MigrationManager.registerMigration(new CreatePlayerVaultItem());
        MigrationManager.registerMigration(new CreatePlayerVault());
        MigrationManager.registerMigration(new CreatePlayerSlots());
        MigrationManager.registerMigration(new UpdateUserTableAddFreezeColumn());
        MigrationManager.registerMigration(new UpdateUserTableAddFlyColumn());
        MigrationManager.registerMigration(new UpdateEconomyTransactionAddColumn());

        // Repositories
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
        this.repositories.register(ServerStorageRepository.class);
        this.repositories.register(VoteSiteRepository.class);
        this.repositories.register(PlayerSlotRepository.class);
        this.repositories.register(VaultItemRepository.class);
        this.repositories.register(VaultRepository.class);

        MigrationManager.execute(this.connection, JULogger.from(this.plugin.getLogger()));

        with(UserCooldownsRepository.class).deleteExpiredCooldowns();
        with(UserRepository.class).clearExpiredSanctions();
        with(UserMailBoxRepository.class).deleteExpiredItems();
        this.setActiveSanctions(with(UserSanctionRepository.class).getActiveBan());

        List<ServerStorageDTO> serverStorageDTOS = with(ServerStorageRepository.class).select();
        plugin.getServerStorage().setContents(serverStorageDTOS);
    }

    private @NotNull DatabaseConfiguration getDatabaseConfiguration(EssentialsPlugin plugin, StorageType storageType) {
        DatabaseConfiguration oldConfiguration = plugin.getConfiguration().getDatabaseConfiguration();
        return new DatabaseConfiguration(oldConfiguration.getTablePrefix(), oldConfiguration.getUser(), oldConfiguration.getPassword(), oldConfiguration.getPort(), oldConfiguration.getHost(), oldConfiguration.getDatabase(), oldConfiguration.isDebug(), storageType == StorageType.SQLITE ? DatabaseType.SQLITE : DatabaseType.MYSQL);
    }

    @Override
    public void onEnable() {

        this.connection.connect();
        this.totalUser = with(UserRepository.class).totalUsers();
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

            List<UserDTO> userDTOS = with(UserRepository.class).selectUser(uniqueId);
            // First join!
            if (userDTOS.isEmpty()) {
                this.firstJoin(user);
            }

            with(UserRepository.class).upsert(uniqueId, playerName); // Create the player or update his name
            if (!userDTOS.isEmpty()) {

                UserDTO userDTO = userDTOS.get(0);

                if (userDTO.mute_sanction_id() != null) { // Check if the player is mute
                    SanctionDTO sanction = with(UserSanctionRepository.class).getSanction(userDTO.mute_sanction_id());
                    if (sanction.isActive()) {
                        user.setMuteSanction(Sanction.fromDTO(sanction));
                    }
                }

                // ToDo, gérer les requêtes des joueurs en fonction de si le module est activé ou non, pour éviter de faire des requêtes dans le vide

                user.setSanction(userDTO.ban_sanction_id(), userDTO.mute_sanction_id());
                user.setWithDTO(userDTO);
                user.setOptions(with(UserOptionRepository.class).select(uniqueId));
                user.setCooldowns(with(UserCooldownsRepository.class).select(uniqueId));
                user.setEconomies(with(UserEconomyRepository.class).select(uniqueId));
                user.setHomes(with(UserHomeRepository.class).select(uniqueId));
                user.setPowerTools(with(UserPowerToolsRepository.class).select(uniqueId).stream().collect(Collectors.toMap(PowerToolsDTO::material, PowerToolsDTO::command)));
                user.setMailBoxItems(with(UserMailBoxRepository.class).select(uniqueId));
                user.setVoteSites(with(VoteSiteRepository.class).select(uniqueId));
            }
        });

        return user;
    }

    public <T extends Repository> T with(Class<T> module) {
        return this.repositories.getTable(module);
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
        async(() -> with(UserOptionRepository.class).upsert(uniqueId, option, value));
    }

    @Override
    public void updateCooldown(UUID uniqueId, String key, long expiredAt) {
        async(() -> with(UserCooldownsRepository.class).upsert(uniqueId, key, expiredAt));
    }

    @Override
    public void deleteCooldown(UUID uniqueId, String key) {
        async(() -> with(UserCooldownsRepository.class).delete(uniqueId, key));
    }

    @Override
    public void updateEconomy(UUID uniqueId, Economy economy, BigDecimal bigDecimal) {
        async(() -> with(UserEconomyRepository.class).upsert(uniqueId, economy, bigDecimal));
    }

    @Override
    public void upsertUser(User user) {
        async(() -> with(UserRepository.class).upsert(user));
    }

    @Override
    public void updateUserMoney(UUID uniqueId, Consumer<User> consumer) {
        User fakeUser = new ZUser(this.plugin, uniqueId);
        fakeUser.setEconomies(with(UserEconomyRepository.class).select(uniqueId));
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

                consumer.accept(with(UserEconomyRepository.class).select(uuid));
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
                List<UserDTO> userDTOS = with(UserRepository.class).selectUsers(userName);
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
    public void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount, String reason) {
        async(() -> with(EconomyTransactionsRepository.class).upsert(fromUuid, toUuid, economy, fromAmount, toAmount, reason));
    }

    @Override
    public List<EconomyTransactionDTO> getTransactions(UUID toUuid, Economy economy) {
        return with(EconomyTransactionsRepository.class).selectTransactions(toUuid, economy);
    }

    @Override
    public void upsertStorage(String key, Object value) {
        // async(() -> repo(ServerStorageRepository.class).upsert(key, value));
    }

    @Override
    public void upsertHome(UUID uniqueId, Home home) {
        async(() -> with(UserHomeRepository.class).upsert(uniqueId, home));
    }

    @Override
    public void deleteHome(UUID uniqueId, String name) {
        async(() -> with(UserHomeRepository.class).deleteHome(uniqueId, name));
    }

    @Override
    public CompletableFuture<List<Home>> getHome(UUID uuid, String homeName) {
        CompletableFuture<List<Home>> future = new CompletableFuture<>();
        future.complete(with(UserHomeRepository.class).getHomes(uuid, homeName));
        return future;
    }

    @Override
    public CompletionStage<List<Home>> getHomes(UUID uuid) {
        CompletableFuture<List<Home>> future = new CompletableFuture<>();
        future.complete(with(UserHomeRepository.class).getHomes(uuid));
        return future;
    }

    @Override
    public void insertSanction(Sanction sanction, Consumer<Integer> consumer) {
        if (sanction.getSanctionType() == SanctionType.BAN) {
            this.banSanctions.put(sanction.getPlayerUniqueId(), sanction);
        } else if (sanction.getSanctionType() == SanctionType.UNBAN) {
            this.banSanctions.remove(sanction.getPlayerUniqueId());
        }

        async(() -> with(UserSanctionRepository.class).insert(sanction, consumer));
    }

    @Override
    public void updateUserBan(UUID uuid, Integer index) {
        if (index == null) this.banSanctions.remove(uuid);
        async(() -> with(UserRepository.class).updateBanId(uuid, index));
    }

    @Override
    public void updateUserMute(UUID uuid, Integer index) {
        async(() -> with(UserRepository.class).updateMuteId(uuid, index));
    }

    @Override
    public boolean isMute(UUID uuid) {
        Sanction sanction = getMute(uuid);
        return sanction != null && sanction.isActive();
    }

    @Override
    public Sanction getMute(UUID uuid) {
        List<UserDTO> userDTOS = with(UserRepository.class).selectUser(uuid);
        if (userDTOS.isEmpty()) return null;

        UserDTO userDTO = userDTOS.get(0);

        if (userDTO.mute_sanction_id() != null) {
            SanctionDTO sanction = with(UserSanctionRepository.class).getSanction(userDTO.mute_sanction_id());
            return Sanction.fromDTO(sanction);
        }
        return null;
    }

    @Override
    public List<SanctionDTO> getSanctions(UUID uuid) {
        return with(UserSanctionRepository.class).getSanctions(uuid);
    }

    @Override
    public void insertChatMessage(UUID uuid, String content) {
        async(() -> with(ChatMessagesRepository.class).insert(new ChatMessageDTO(uuid, content, new Date())));
    }

    @Override
    public void insertCommand(UUID uuid, String command) {
        async(() -> with(CommandsRepository.class).insert(new CommandDTO(uuid, command, new Date())));
    }

    @Override
    public void insertPlayTime(UUID uniqueId, long sessionPlayTime, long playtime, String address) {
        async(() -> {
            if (sessionPlayTime > 0) {
                with(UserPlayTimeRepository.class).insert(uniqueId, sessionPlayTime, address);
            }
            with(UserRepository.class).updatePlayTime(uniqueId, playtime);
        });
    }

    @Override
    public List<UserDTO> getUsers(String ip) {
        return with(UserRepository.class).getUsers(ip);
    }

    @Override
    public UserRecord fetchUserRecord(UUID uuid) {

        UserDTO userDTO = with(UserRepository.class).selectUser(uuid).get(0);
        List<PlayTimeDTO> playTimeDTOS = with(UserPlayTimeRepository.class).select(uuid);

        return new UserRecord(userDTO, playTimeDTOS);
    }

    @Override
    public List<ChatMessageDTO> getMessages(UUID targetUuid) {
        return with(ChatMessagesRepository.class).getMessages(targetUuid);
    }

    @Override
    public Map<Option, Boolean> getOptions(UUID uuid) {
        if (this.users.containsKey(uuid)) {
            return this.users.get(uuid).getOptions();
        }
        return with(UserOptionRepository.class).select(uuid).stream().collect(Collectors.toMap(OptionDTO::option_name, OptionDTO::option_value));
    }

    @Override
    public List<CooldownDTO> getCooldowns(UUID uniqueId) {
        return with(UserCooldownsRepository.class).select(uniqueId);
    }

    @Override
    public void setPowerTools(UUID uniqueId, Material material, String command) {
        async(() -> with(UserPowerToolsRepository.class).upsert(uniqueId, material, command));
    }

    @Override
    public void deletePowerTools(UUID uniqueId, Material material) {
        async(() -> with(UserPowerToolsRepository.class).delete(uniqueId, material));
    }

    @Override
    public void addMailBoxItem(MailBoxItem mailBoxItem) {
        async(() -> with(UserMailBoxRepository.class).insert(mailBoxItem));
    }

    @Override
    public void removeMailBoxItem(int id) {
        async(() -> with(UserMailBoxRepository.class).delete(id));
    }

    @Override
    public List<UserEconomyRankingDTO> getEconomyRanking(Economy economy) {
        return with(UserRepository.class).getBalanceRanking(economy.getName());
    }

    @Override
    public List<MailBoxDTO> getMailBox(UUID uuid) {
        return with(UserMailBoxRepository.class).select(uuid);
    }

    @Override
    public void fetchOfflinePlayerEconomies(Consumer<List<UserEconomyDTO>> consumer) {
        async(() -> consumer.accept(with(UserEconomyRepository.class).getAll()));
    }

    @Override
    public void setVote(UUID uuid, long vote, long offline) {
        async(() -> with(UserRepository.class).setVote(uuid, vote, offline));
    }

    @Override
    public UserVoteDTO getVote(UUID uniqueId) {
        var user = getUser(uniqueId);
        if (user != null) return new UserVoteDTO(uniqueId, user.getVote(), 0);

        var users = with(UserRepository.class).selectVoteUser(uniqueId);
        return users.isEmpty() ? new UserVoteDTO(uniqueId, 0, 0) : users.get(0);
    }

    @Override
    public void updateServerStorage(String key, Object object) {
        async(() -> with(ServerStorageRepository.class).upsert(key, object));
    }

    @Override
    public void setLastVote(UUID uniqueId, String site) {
        async(() -> with(VoteSiteRepository.class).setLastVote(uniqueId, site));
    }

    @Override
    public void resetVotes() {
        async(() -> with(UserRepository.class).resetVotes());
    }

    @Override
    public void updateVaultQuantity(UUID uniqueId, int vaultId, int slot, long quantity) {
        async(() -> with(VaultItemRepository.class).updateQuantity(uniqueId, vaultId, slot, quantity));
    }

    @Override
    public void removeVaultItem(UUID uniqueId, int vaultId, int slot) {
        async(() -> with(VaultItemRepository.class).removeItem(uniqueId, vaultId, slot));
    }

    @Override
    public void createVaultItem(UUID uniqueId, int vaultId, int slot, long quantity, String item) {
        async(() -> with(VaultItemRepository.class).createNewItem(uniqueId, vaultId, slot, quantity, item));
    }

    @Override
    public void setVaultSlot(UUID uniqueId, int slots) {
        async(() -> with(PlayerSlotRepository.class).setSlot(uniqueId, slots));
    }

    @Override
    public List<VaultDTO> getVaults() {
        return with(VaultRepository.class).select();
    }

    @Override
    public List<VaultItemDTO> getVaultItems() {
        return with(VaultItemRepository.class).select();
    }

    @Override
    public List<PlayerSlotDTO> getPlayerVaultSlots() {
        return with(PlayerSlotRepository.class).select();
    }

    @Override
    public void updateVault(UUID uniqueId, Vault vault) {
        async(() -> with(VaultRepository.class).update(uniqueId, vault.getVaultId(), vault.getName(), vault.getIconItemStack() == null ? null : ItemStackUtils.serializeItemStack(vault.getIconItemStack())));
    }

    @Override
    public void updateUserFrozen(UUID uuid, boolean frozen) {
        async(() -> with(UserRepository.class).updateFrozen(uuid, frozen));
    }

    @Override
    public void upsertFlySeconds(UUID uniqueId, long flySeconds) {
        async(() -> with(UserRepository.class).upsertFly(uniqueId, flySeconds));
    }

    @Override
    public long getFlySeconds(UUID uniqueId) {
        return with(UserRepository.class).selectFly(uniqueId);
    }

    @Override
    public void deleteWorldData(String worldName) {
        with(UserRepository.class).deleteWorldData(worldName);
        with(UserHomeRepository.class).deleteWorldData(worldName);
    }

    public DatabaseConnection getConnection() {
        return connection;
    }
}
