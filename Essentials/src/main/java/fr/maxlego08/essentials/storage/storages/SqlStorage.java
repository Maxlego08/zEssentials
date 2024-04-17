package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.SanctionDTO;
import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.database.Repositories;
import fr.maxlego08.essentials.storage.database.SqlConnection;
import fr.maxlego08.essentials.storage.database.repositeries.EconomyTransactionsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserCooldownsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserOptionRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserSanctionRepository;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class SqlStorage extends StorageHelper implements IStorage {

    private final SqlConnection connection;
    private final Repositories repositories;

    public SqlStorage(EssentialsPlugin plugin) {
        super(plugin);
        this.connection = new SqlConnection(plugin);

        if (!this.connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database !");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            plugin.getLogger().info("The database connection is valid ! (" + connection.getDatabaseConfiguration().host() + ")");
        }

        this.repositories = new Repositories(this.connection);
        this.repositories.register(UserRepository.class);
        this.repositories.register(UserOptionRepository.class);
        this.repositories.register(UserCooldownsRepository.class);
        this.repositories.register(UserEconomyRepository.class);
        this.repositories.register(EconomyTransactionsRepository.class);
        this.repositories.register(UserHomeRepository.class);
        this.repositories.register(UserSanctionRepository.class);
        // this.repositories.register(ServerStorageRepository.class);

        plugin.getMigrationManager().execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.plugin.getLogger());

        this.repositories.getTable(UserCooldownsRepository.class).deleteExpiredCooldowns();
        this.repositories.getTable(UserRepository.class).clearExpiredSanctions();
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
                user.setOptions(this.repositories.getTable(UserOptionRepository.class).selectOptions(uniqueId));
                user.setCooldowns(this.repositories.getTable(UserCooldownsRepository.class).selectCooldowns(uniqueId));
                user.setEconomies(this.repositories.getTable(UserEconomyRepository.class).selectEconomies(uniqueId));
                user.setHomes(this.repositories.getTable(UserHomeRepository.class).selectHomes(uniqueId));
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
}
