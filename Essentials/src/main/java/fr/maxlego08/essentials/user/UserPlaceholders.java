package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;

public class UserPlaceholders implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        IStorage iStorage = plugin.getStorageManager().getStorage();

        placeholder.register("user_target_player_name", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user.getTargetUser() != null ? user.getTargetUser().getName() : "no";
        });

    }
}
