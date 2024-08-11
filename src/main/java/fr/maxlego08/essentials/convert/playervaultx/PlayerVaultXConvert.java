package fr.maxlego08.essentials.convert.playervaultx;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.File;
import java.util.UUID;

public class PlayerVaultXConvert extends ZUtils implements Convert {

    private final EssentialsPlugin plugin;

    public PlayerVaultXConvert(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void convert(CommandSender sender) {


        if (!Bukkit.getPluginManager().isPluginEnabled("PlayerVaults")) {
            message(sender, "&cYou must have PlayerVaultX on your server to do the conversion.");
            return;
        }

        File file = new File(Bukkit.getWorldContainer(), "plugins/PlayerVaults/newvaults");
        if (!file.exists()) {
            message(sender, "&cUnable to find &bplugins/PlayerVaults/newvaults&c.");
            return;
        }

        message(sender, "&fStart convert &7PlayerVaultX");

        if (this.plugin.getStorageManager().getStorage() instanceof SqlStorage) {

            this.plugin.getScheduler().runAsync(wrappedTask -> startConvertDatabase(sender, file));
        } else {

            message(sender, "&cYou must have the storage in a database to be able to convert. Never use the storage in JSON !");
        }
    }

    private void startConvertDatabase(CommandSender sender, File folder) {

        int vaults = 0;
        int players = 0;
        var manager = plugin.getVaultManager();

        var files = folder.listFiles();
        if (files == null) {
            message(sender, "§cUnable to find files from vaults.");
            return;
        }

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;
            UUID uuid;
            try {
                uuid = UUID.fromString(file.getName().replace(".yml", ""));
            } catch (Exception ignored) {
                continue;
            }
            players++;


            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (String vaultName : configuration.getKeys(false)) {

                ItemStack[] itemStacks = getItems(configuration.getString(vaultName));
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack != null && !itemStack.getType().isAir()) {
                        manager.addItem(uuid, itemStack);
                    }
                }

                vaults++;

            }
        }

        message(sender, "&aYou have just converted your PlayerVaultX data to zEssentials !");
        message(sender, "&f" + players + " §aplayers and &f" + vaults + " §avaults.");

    }

    private ItemStack[] getItems(String base64) {
        try {
            var bytes = Base64Coder.decodeLines(base64);
            var cardBoxSerializationClass = Class.forName("com.drtshock.playervaults.vaultmanagement.CardboardBoxSerialization");
            var method = cardBoxSerializationClass.getDeclaredMethod("readInventory", byte[].class);
            method.setAccessible(true);
            return (ItemStack[]) method.invoke(null, bytes);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ItemStack[0];
    }

}
