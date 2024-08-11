package fr.maxlego08.essentials.convert.sunlight;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.SqliteConnection;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

public class SunlightConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public SunlightConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void convert(CommandSender sender) {

        message(sender, "&fStart convert &7Sunlight");
        File file = new File(this.plugin.getDataFolder(), "data.db");
        if (!file.exists()) {
            message(sender, "&cUnable to find &bdata.db &cfile in &fplugins/zEssentials&c.");
            return;
        }

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage sqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> startConvertDatabase(sender, sqlStorage));
        } else {

            message(sender, "&cYou must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender, SqlStorage sqlStorage) {
        var databaseConnection = new SqliteConnection(DatabaseConfiguration.sqlite(sqlStorage.getConnection().getDatabaseConfiguration().isDebug()), plugin.getDataFolder());
        databaseConnection.setFileName("data.db");

        if (!databaseConnection.isValid()) {
            message(sender, "&cUnable to connect to database.");
        }

        RequestHelper requestHelper = new RequestHelper(databaseConnection, message -> plugin.getLogger().info(message));
        List<SunlightUser> sunlightUsers = requestHelper.select("sunlight_users", SunlightUser.class, table -> {
        });
        List<SunlightHome> sunlightHomes = requestHelper.select("sunlight_homes", SunlightHome.class, table -> {
        });

        message(sender, "&aFound &f" + sunlightUsers.size() + " &ausers and &f" + sunlightHomes.size() + " &ahomes.");

        var userRepository = sqlStorage.with(UserRepository.class);
        var userHomeRepository = sqlStorage.with(UserHomeRepository.class);

        sunlightUsers.forEach(sunlightUser -> {

            if (sunlightUser.name() == null || sunlightUser.uuid() == null) return;

            userRepository.upsert(sunlightUser);
        });

        sunlightHomes.forEach(sunlightHome -> storeHomes(userHomeRepository, sunlightHome));

        message(sender, "&aYou have just converted your Sunlight data to zEssentials !");
    }

    private void storeHomes(UserHomeRepository userHomeRepository, SunlightHome sunlightHome) {
        try {

            var home = parseHomes(sunlightHome);
            userHomeRepository.upsert(sunlightHome.ownerId(), home);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Home parseHomes(SunlightHome sunlightHome) {
        String[] location = sunlightHome.location().split(",");
        String homeLocation = location[5] + "," + location[0] + "," + location[1] + "," + location[2] + "," + location[3] + "," + location[4];
        return new ZHome(stringAsLocation(homeLocation), sunlightHome.name(), null);
    }

}
