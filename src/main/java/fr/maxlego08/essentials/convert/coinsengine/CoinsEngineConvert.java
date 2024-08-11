package fr.maxlego08.essentials.convert.coinsengine;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.SqliteConnection;
import fr.maxlego08.sarah.database.Schema;
import fr.maxlego08.sarah.logger.JULogger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoinsEngineConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public CoinsEngineConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void convert(CommandSender sender) {

        message(sender, "&fStart convert &7CoinsEngine");
        File folder = new File(Bukkit.getWorldContainer(), "plugins/CoinsEngine");
        if (!folder.exists()) {
            message(sender, "&cUnable to find &bplugins/CoinsEngine&c.");
            return;
        }

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage sqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> {
                try {
                    startConvertDatabase(sender, sqlStorage, folder);
                } catch (SQLException exception) {
                    message(sender, "&cImpossible to convert CoinsEngine: " + exception.getMessage());
                    exception.printStackTrace();
                }
            });
        } else {

            message(sender, "&cYou must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender, SqlStorage sqlStorage, File folder) throws SQLException {
        var databaseConnection = new SqliteConnection(DatabaseConfiguration.sqlite(sqlStorage.getConnection().getDatabaseConfiguration().isDebug()), folder);
        databaseConnection.setFileName("data.db");

        if (!databaseConnection.isValid()) {
            message(sender, "&cUnable to connect to database.");
        }

        List<String> columns = getColumns(folder);
        EconomyManager economyManager = plugin.getEconomyManager();

        for (String column : columns) {
            if (economyManager.getEconomy(column).isEmpty()) {
                message(sender, "&cUnable to find the economy §f" + column + "§c. You must create the savings in the §fmodules/economy/config.yml §cfile before converting the data.");
                return;
            }
        }

        var logger = JULogger.from(this.plugin.getLogger());

        var userEconomyRepository = sqlStorage.with(UserEconomyRepository.class);

        Schema schema = SchemaBuilder.select("coinsengine_users");
        var result = schema.executeSelect(databaseConnection, logger);

        message(sender, "&aFound &f" + result.size() + " &ausers.");

        result.forEach(map -> {

            UUID uuid = UUID.fromString((String) map.get("uuid"));
            columns.forEach(column -> {
                Number number = (Number) map.get(column);

                var economy = economyManager.getEconomy(column).get();
                userEconomyRepository.upsert(uuid, economy, convertToBigDecimal(number));
            });
        });

        message(sender, "&aYou have just converted your CoinsEngine data to zEssentials !");
    }

    private List<String> getColumns(File folder) {

        File currenciesFolder = new File(folder, "currencies");
        if (!currenciesFolder.exists()) return new ArrayList<>();

        var files = currenciesFolder.listFiles();
        if (files == null || files.length == 0) return new ArrayList<>();

        List<String> currencies = new ArrayList<>();
        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            String column = configuration.getString("Column_Name");
            if (column != null) {
                currencies.add(column);
            }
        }
        return currencies;
    }

    private BigDecimal convertToBigDecimal(Number number) {
        if (number instanceof Integer || number instanceof Long || number instanceof Short || number instanceof Byte) {
            return BigDecimal.valueOf(number.longValue());
        } else if (number instanceof Double) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return new BigDecimal(number.toString());
    }
}
