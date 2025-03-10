package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

public class WorldEditPlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        var iStorage = plugin.getStorageManager().getStorage();
        WorldeditManager manager = plugin.getWorldeditManager();

        placeholder.register("user_worldedit_option_inventory", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null ? String.valueOf(user.getOption(Option.WORLDEDIT_INVENTORY)) : "false";
        }, "Returns true or false is the inventory option is active");

        placeholder.register("user_worldedit_option_bossbar", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null ? String.valueOf(user.getOption(Option.WORLDEDIT_BOSSBAR_DISABLE)) : "false";
        }, "Returns true or false is the bossbar option is active");
    }
}
