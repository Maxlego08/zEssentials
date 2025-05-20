package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.buttons.ButtonOption;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ButtonOptionLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonOptionLoader(EssentialsPlugin plugin) {
        super(plugin, "ZESSENTIALS_OPTION");
        this.plugin = plugin;
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
        return this.plugin.getInventoryManager().loadItemStack(configuration, path, file);
    }
}
