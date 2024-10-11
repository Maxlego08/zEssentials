package fr.maxlego08.essentials.task;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class FlyTask extends ZUtils {


    public FlyTask(EssentialsPlugin plugin) {
        plugin.getScheduler().runTimer(task -> {

            if (!plugin.isEnabled()) {
                task.cancel();
                return;
            }

            // Fly
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.isFlying() && !player.hasPermission(Permission.ESSENTIALS_FLY_UNLIMITED.asPermission())) {
                    User user = plugin.getUser(player.getUniqueId());
                    if (user == null) continue;

                    long seconds = user.getFlySeconds() - 1;
                    if (seconds <= 0) {

                        player.setAllowFlight(false);
                        player.setFlying(false);
                        message(player, Message.COMMAND_FLY_END);

                    }
                    user.setFlySeconds(seconds);
                }
            }

        }, 1, 1, TimeUnit.SECONDS);
    }

}
