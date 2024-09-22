package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.util.Optional;

public class UserHomePlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        IStorage iStorage = plugin.getStorageManager().getStorage();

        // Home count
        placeholder.register("home_count", (player) -> String.valueOf(Optional.ofNullable(iStorage.getUser(player.getUniqueId()))
                .map(User::countHomes).orElse(0)), "Returns the number of homes");

        // Max home
        placeholder.register("home_max", (player) -> String.valueOf(plugin.getMaxHome(player)), "Returns the number of max homes");

        // Check if home exist
        placeholder.register("home_exist_", (player, homeName) -> Optional.ofNullable(iStorage.getUser(player.getUniqueId()))
                .map(user -> String.valueOf(user.isHomeName(homeName))).orElse("false"), "Returns true if home exists, otherwise false", "home name");

        // Delete home name
        placeholder.register("home_delete", (player, homeName) -> Optional.ofNullable(iStorage.getUser(player.getUniqueId()))
                .map(user -> user.getCurrentDeleteHome().map(Home::getName).orElse("?")).orElse("?"), "Returns the name of the home that the player wants to delete");

    }
}
