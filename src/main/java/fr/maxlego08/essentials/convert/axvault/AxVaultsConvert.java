package fr.maxlego08.essentials.convert.axvault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class AxVaultsConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public AxVaultsConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void convert(CommandSender sender) {


        if (!Bukkit.getPluginManager().isPluginEnabled("AxVaults")) {
            message(sender, "&cYou must have AxVaults on your server to do the conversion.");
            return;
        }

        message(sender, "&fStart convert &7AxVaults");

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> startConvertDatabase(sender));
        } else {

            message(sender, "&cYou must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender) {

        this.plugin.createInstance("AxVaultsHook");
        message(sender, "&aYou have just converted your AxVaults data to zEssentials !");
    }
}
