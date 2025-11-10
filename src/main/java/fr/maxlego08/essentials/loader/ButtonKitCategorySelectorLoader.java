package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.kit.ButtonKitCategorySelector;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonKitCategorySelectorLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonKitCategorySelectorLoader(EssentialsPlugin plugin) {
        super(plugin, "zessentials_kit_category_selector");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration yamlConfiguration, String s, DefaultButtonValue defaultButtonValue) {
        String categoryName = yamlConfiguration.getString(s + "category");
        return new ButtonKitCategorySelector(this.plugin, categoryName);
    }
}
