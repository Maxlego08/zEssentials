package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.kit.ButtonKitSubCategory;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonKitSubCategoryLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonKitSubCategoryLoader(EssentialsPlugin plugin) {
        super(plugin, "zessentials_kit_subcategory");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration yamlConfiguration, String s, DefaultButtonValue defaultButtonValue) {
        String categoryName = yamlConfiguration.getString(s + "category");
        String subCategoryName = yamlConfiguration.getString(s + "subcategory");
        return new ButtonKitSubCategory(this.plugin, categoryName, subCategoryName);
    }
}
