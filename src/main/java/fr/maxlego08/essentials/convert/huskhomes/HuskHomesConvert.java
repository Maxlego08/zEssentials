package fr.maxlego08.essentials.convert.huskhomes;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.SqliteConnection;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

public class HuskHomesConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public HuskHomesConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public void convert(CommandSender sender) {

        message(sender, "&fStart convert &7HuskHomes");
        /*File file = new File(this.plugin.getDataFolder(), "HuskHomesData.db");
        if (!file.exists()) {
            message(sender, "&cUnable to find &bHuskHomesData.db &cfile in &fplugins/zEssentials&c.");
            return;
        }*/

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage sqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> startConvertDatabase(sender, sqlStorage));
        } else {

            message(sender, "&cYou must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender, SqlStorage sqlStorage) {

        DatabaseConnection databaseConnection = sqlStorage.getConnection();
        File file = new File(this.plugin.getDataFolder(), "HuskHomesData.db");
        if (file.exists()) {
            var sqliteConnection = new SqliteConnection(DatabaseConfiguration.sqlite(sqlStorage.getConnection().getDatabaseConfiguration().isDebug()), this.plugin.getDataFolder());
            sqliteConnection.setFileName("HuskHomesData.db");
            databaseConnection = sqliteConnection;

            if (!databaseConnection.isValid()) {
                message(sender, "&cUnable to connect to database.");
                return;
            }
        }

        RequestHelper requestHelper = new RequestHelper(databaseConnection, message -> plugin.getLogger().info(message));
        List<HuskUser> users = requestHelper.selectAll("huskhomes_users", HuskUser.class);
        List<HuskHome> homes = requestHelper.selectAll("huskhomes_homes", HuskHome.class);
        List<HuskPosition> positions = requestHelper.selectAll("huskhomes_position_data", HuskPosition.class);
        List<HuskSavedPosition> savedPositions = requestHelper.selectAll("huskhomes_saved_positions", HuskSavedPosition.class);

        var userHomeRepository = sqlStorage.with(UserHomeRepository.class);
        var userRepository = sqlStorage.with(UserRepository.class);

        message(sender, "&aFound &f" + homes.size() + " &ahomes and §f" + users.size() + " §ausers.");

        users.forEach(user -> userRepository.upsert(user.uuid(), user.username()));

        homes.forEach(home -> {

            var optional = savedPositions.stream().filter(position -> position.id() == home.saved_position_id()).findFirst();
            if (optional.isEmpty()) {
                this.plugin.getLogger().severe("Impossible to find home saved position with id " + home.saved_position_id() + " for home " + home.uuid());
                return;
            }

            var savedPosition = optional.get();

            var optionalPosition = positions.stream().filter(position -> position.id() == savedPosition.position_id()).findFirst();
            if (optionalPosition.isEmpty()) {
                this.plugin.getLogger().severe("Impossible to find home position with id " + savedPosition.position_id() + " for home " + home.uuid());
                return;
            }

            var position = optionalPosition.get();

            SafeLocation location = new SafeLocation(position.world_name(), position.x(), position.y(), position.z(), (float) position.yaw(), (float) position.pitch());
            userHomeRepository.upsert(home.owner_uuid(), new ZHome(location, savedPosition.name(), null));
        });

        message(sender, "&aYou have just converted your HuskHomes data to zEssentials !");
    }
}
