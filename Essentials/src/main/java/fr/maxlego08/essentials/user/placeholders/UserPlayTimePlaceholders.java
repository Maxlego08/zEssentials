package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

public class UserPlayTimePlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        IStorage iStorage = plugin.getStorageManager().getStorage();

        placeholder.register("user_play_time", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return String.valueOf(user.getPlayTime());
        }, "Returns the player’s playing time");

        placeholder.register("user_play_time_formatted", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return TimerBuilder.getStringTime(user.getPlayTime() * 1000);
        }, "Returns the player’s playing time formatted");

        placeholder.register("user_current_session_play_time", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return String.valueOf((System.currentTimeMillis() - user.getCurrentSessionPlayTime()) / 1000);
        }, "Returns the player’s playing time of the current session");

        placeholder.register("user_current_session_play_time_formatted", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return TimerBuilder.getStringTime(System.currentTimeMillis() - user.getCurrentSessionPlayTime());
        }, "Returns the player’s playing time of the current session formatted");
    }
}
