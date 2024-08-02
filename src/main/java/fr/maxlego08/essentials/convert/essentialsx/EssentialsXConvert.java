package fr.maxlego08.essentials.convert.essentialsx;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EssentialsXConvert extends ZUtils {

    private final EssentialsPlugin plugin;

    public EssentialsXConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public void convert(CommandSender sender) {

        message(sender, "&fStart convert &7EssentialX");
        File folder = new File("plugins/Essentials/userdata");
        if (!folder.exists()) {
            message(sender, "&cUnable to find the folder &bplugins/Essentials/userdata&c.");
            return;
        }

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage sqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> startConvertDatabase(sender, sqlStorage, folder));
        } else {

            message(sender, "&cYou must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender, SqlStorage sqlStorage, File folder) {

        var files = folder.listFiles();
        if (files == null || files.length == 0) {
            message(sender, "&cNo files found in the userdata folder!");
            return;
        }

        message(sender, "&aFound &f" + files.length + " &ausers.");

        for (File file : files) {
            loadUser(sqlStorage, file);
        }

        message(sender, "&aYou have just converted your EssentialsX data to zEssentials !");
    }

    private void loadUser(SqlStorage sqlStorage, File file) {

        String fileName = file.getName();
        if (!fileName.endsWith(".yml")) return;

        try {

            UUID uniqueId = UUID.fromString(fileName.replace(".yml", ""));
            var userRepository = sqlStorage.with(UserRepository.class);
            var userEconomyRepository = sqlStorage.with(UserEconomyRepository.class);
            var userHomeRepository = sqlStorage.with(UserHomeRepository.class);

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            String money = configuration.getString("money", "0");

            this.plugin.getScheduler().runAsync(wrappedTask -> {

                userRepository.upsert(uniqueId, configuration.getString("last-account-name"));
                userEconomyRepository.upsert(uniqueId, plugin.getEconomyManager().getDefaultEconomy(), new BigDecimal(money));

                var homes = parseHomes(configuration);
                homes.forEach(home -> userHomeRepository.upsert(uniqueId, home));

            });

        } catch (Exception exception) {
            exception.printStackTrace();
            this.plugin.getLogger().severe("Impossible to load the file " + file.getName());
        }
    }

    private List<Home> parseHomes(YamlConfiguration configuration) {
        List<Home> homes = new ArrayList<>();

        ConfigurationSection configurationSection = configuration.getConfigurationSection("homes");

        if (configurationSection != null) {
            configurationSection.getKeys(false).forEach(homeName -> {

                World world = Bukkit.getWorld(configurationSection.getString(homeName + ".world-name", ""));
                double x = configurationSection.getDouble(homeName + ".x");
                double y = configurationSection.getDouble(homeName + ".y");
                double z = configurationSection.getDouble(homeName + ".z");
                double yaw = configurationSection.getDouble(homeName + ".yaw");
                double pitch = configurationSection.getDouble(homeName + ".pitch");

                if (world == null) return;

                var home = new ZHome(new Location(world, x, y, z, (float) yaw, (float) pitch), homeName, null);
                homes.add(home);
            });
        }

        return homes;
    }

}
