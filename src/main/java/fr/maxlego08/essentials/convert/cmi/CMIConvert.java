package fr.maxlego08.essentials.convert.cmi;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.SqliteConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CMIConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public CMIConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void convert(CommandSender sender) {

        message(sender, "&fStart convert &7CMI");
        File file = new File(this.plugin.getDataFolder(), "cmi.sqlite.db");
        if (!file.exists()) {
            message(sender, "&cUnable to find &bcmi.sqlite.db &cfile in &fplugins/zEssentials&c.");
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
        databaseConnection.setFileName("cmi.sqlite.db");

        if (!databaseConnection.isValid()) {
            message(sender, "&cUnable to connect to database.");
        }

        RequestHelper requestHelper = new RequestHelper(databaseConnection, message -> plugin.getLogger().info(message));
        List<CMIUser> cmiUsers = requestHelper.select("users", CMIUser.class, table -> {
        });

        message(sender, "&aFound &f" + cmiUsers.size() + " &ausers.");

        var userRepository = sqlStorage.with(UserRepository.class);
        var userEconomyRepository = sqlStorage.with(UserEconomyRepository.class);
        var userHomeRepository = sqlStorage.with(UserHomeRepository.class);

        cmiUsers.forEach(cmiUser -> {

            if (cmiUser.username() == null || cmiUser.player_uuid() == null) return;

            userRepository.upsert(cmiUser);

            if (this.plugin.getEconomyManager().isEnable() && cmiUser.Balance() != null) {
                var defaultEconomy = this.plugin.getEconomyManager().getDefaultEconomy();
                userEconomyRepository.upsert(cmiUser.player_uuid(), defaultEconomy, cmiUser.Balance());
            }

            var homes = cmiUser.Homes();
            if (homes != null) {
                storeHomes(userHomeRepository, cmiUser);
            }
        });

        message(sender, "&aYou have just converted your CMI data to zEssentials !");
    }

    private void storeHomes(UserHomeRepository userHomeRepository, CMIUser cmiUser) {
        try {

            var homes = parseHomes(cmiUser.Homes());
            homes.forEach(home -> userHomeRepository.upsert(cmiUser.player_uuid(), home));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<Home> parseHomes(String data) {
        List<Home> homes = new ArrayList<>();
        String[] homeEntries = data.split(";");

        for (String entry : homeEntries) {
            try {
                String[] parts = entry.split("[$]|%%|:");
                String name = parts[0];
                int index = parts.length == 10 ? 4 : parts.length == 9 ? 3 : 2;
                Material material = null;
                if (parts.length == 9) {
                    try {
                        material = Material.valueOf(parts[2].replace("-", ""));
                    } catch (Exception ignored) {
                    }
                }
                String world = parts[index];
                double x = Double.parseDouble(parts[index + 1]);
                double y = Double.parseDouble(parts[index + 2]);
                double z = Double.parseDouble(parts[index + 3]);
                double yaw = Double.parseDouble(parts[index + 4]);
                double pitch = Double.parseDouble(parts[index + 5]);

                var bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld == null) continue;
                homes.add(new ZHome(new Location(bukkitWorld, x, y, z, (float) pitch, (float) yaw), name, material));
            } catch (Exception exception) {
                exception.printStackTrace();
                this.plugin.getLogger().severe("Unable to convert the home " + entry);
            }
        }
        return homes;
    }

}
