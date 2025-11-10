package fr.maxlego08.essentials.task;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FlyTask extends ZUtils {


    public FlyTask(EssentialsPlugin plugin) {
        plugin.getScheduler().runTimer(task -> {

            if (!plugin.isEnabled()) {
                task.cancel();
                return;
            }

            List<Long> announces = plugin.getConfiguration().getFlyTaskAnnounce();

            // Fly
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.isFlying() && !player.hasPermission(Permission.ESSENTIALS_FLY_UNLIMITED.asPermission()) && (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL)) {
                    User user = plugin.getUser(player.getUniqueId());
                    if (user == null) continue;

                    long seconds = user.getFlySeconds() - 1;
                    if (announces.contains(seconds)) {
                        message(player, Message.COMMAND_FLY_REMAINING, "%time%", TimerBuilder.getStringTime(seconds * 1000));
                    }

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
