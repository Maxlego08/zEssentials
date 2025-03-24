package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.buttons.ButtonOption;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class ButtonOptionLoader implements ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonOptionLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return ButtonOption.class;
    }

    @Override
    public String getName() {
        return "ZESSENTIALS_OPTION";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        File file = new File(this.plugin.getDataFolder(), "inventories/options.yml");
        List<Integer> offsetSlots = configuration.getIntegerList(path + "offset-slots");
        Option option = Option.valueOf(configuration.getString(path + "option", Option.PRIVATE_MESSAGE_DISABLE.name()).toUpperCase());
        MenuItemStack enableItemStack = load(configuration, path + "enable-item.", file);
        MenuItemStack disableItemStack = load(configuration, path + "disable-item.", file);
        MenuItemStack offsetEnableItemStack = load(configuration, path + "offset-enable-item.", file);
        MenuItemStack offsetDisableItemStack = load(configuration, path + "offset-disable-item.", file);

        return new ButtonOption(this.plugin, option, offsetSlots, enableItemStack, disableItemStack, offsetEnableItemStack, offsetDisableItemStack);
    }

    private MenuItemStack load(YamlConfiguration configuration, String path, File file) {
        try {
            Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
            return loader.load(configuration, path, file);
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
