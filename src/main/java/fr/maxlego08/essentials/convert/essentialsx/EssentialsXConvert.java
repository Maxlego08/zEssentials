package fr.maxlego08.essentials.convert.essentialsx;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserHomeRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EssentialsXConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public EssentialsXConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void convert(CommandSender sender) {

        message(sender, "<c:#FFFFFF>Start convert <c:#AAAAAA>EssentialX");
        File folder = new File("plugins/Essentials/userdata");
        if (!folder.exists()) {
            message(sender, "<c:#FF5555>Unable to find the folder <c:#55FFFF>plugins/Essentials/userdata<c:#FF5555>.");
            return;
        }

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage sqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> startConvertDatabase(sender, sqlStorage, folder));
        } else {

            message(sender, "<c:#FF5555>You must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender, SqlStorage sqlStorage, File folder) {

        var files = folder.listFiles();
        if (files == null || files.length == 0) {
            message(sender, "<c:#FF5555>No files found in the userdata folder!");
            return;
        }

        message(sender, "<c:#55FF55>Found <c:#FFFFFF>" + files.length + " <c:#55FF55>users.");

        for (File file : files) {
            loadUser(sqlStorage, file, sender);
        }

        message(sender, "<c:#55FF55>You have just converted your EssentialsX data to zEssentials !");
    }

    private void loadUser(SqlStorage sqlStorage, File file, CommandSender sender) {

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

                String lastName = configuration.getString("last-account-name");

                if (lastName == null) {
                    plugin.getLogger().warning("Impossible to find the name of " + uniqueId + ", try with offline player");
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
                    if (offlinePlayer.getName() == null) {
                        plugin.getLogger().warning("Impossible to find the name of " + uniqueId + ", even with an offline player, skip.");
                        return;
                    } else {
                        lastName = offlinePlayer.getName();
                    }
                }

                userRepository.upsert(uniqueId, lastName);
                userEconomyRepository.upsert(uniqueId, plugin.getEconomyManager().getDefaultEconomy(), new BigDecimal(money));

                var homes = parseHomes(configuration);
                homes.forEach(home -> userHomeRepository.upsert(uniqueId, home));

                if (sender instanceof Player player) {
                    this.plugin.getComponentMessage().sendActionBar(player, "<c:#FFFFFF>Convert user <c:#AAAAAA>" + uniqueId);
                }

            });

        } catch (Exception exception) {
            exception.printStackTrace();
            this.plugin.getLogger().severe("Impossible to load the file " + file.getName());
        }

        message(sender, "<c:#55FF55>You have just converted your EssentialsX data to zEssentials !");
    }

    private List<Home> parseHomes(YamlConfiguration configuration) {
        List<Home> homes = new ArrayList<>();

        ConfigurationSection configurationSection = configuration.getConfigurationSection("homes");

        if (configurationSection != null) {
            configurationSection.getKeys(false).forEach(homeName -> {

                String world = configurationSection.getString(homeName + ".world-name", "world");
                double x = configurationSection.getDouble(homeName + ".x");
                double y = configurationSection.getDouble(homeName + ".y");
                double z = configurationSection.getDouble(homeName + ".z");
                double yaw = configurationSection.getDouble(homeName + ".yaw");
                double pitch = configurationSection.getDouble(homeName + ".pitch");

                var home = new ZHome(new SafeLocation(world, x, y, z, (float) yaw, (float) pitch), homeName, null);
                homes.add(home);
            });
        }

        return homes;
    }

}
