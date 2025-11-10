package fr.maxlego08.essentials.task;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FlyTask extends ZUtils {

    // Performance optimization - Process players in batches to prevent lag on high-population servers
    private static final int BATCH_SIZE = 50; 
    private Iterator<? extends Player> playerIterator;
    private List<Player> currentBatch = new ArrayList<>();

    public FlyTask(EssentialsPlugin plugin) {
        plugin.getScheduler().runTimer(task -> {

            if (!plugin.isEnabled()) {
                task.cancel();
                return;
            }

            List<Long> announces = plugin.getConfiguration().getFlyTaskAnnounce();
            Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
            
            // Initialize new iterator or continue with existing one - Prevents processing all players at once
            if (playerIterator == null || !playerIterator.hasNext()) {
                playerIterator = onlinePlayers.iterator();
                currentBatch.clear();
            }

            // Create batch - Process only BATCH_SIZE players per second
            int processed = 0;
            while (playerIterator.hasNext() && processed < BATCH_SIZE) {
                currentBatch.add(playerIterator.next());
                processed++;
            }

            // Process players in current batch
            for (Player player : currentBatch) {
                // Skip if player went offline
                if (!player.isOnline()) continue;
                
                // Flight check - Check players without unlimited permission in survival/adventure mode
                if (player.isFlying() && !player.hasPermission(Permission.ESSENTIALS_FLY_UNLIMITED.asPermission()) 
                    && (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL)) {
                    
                    User user = plugin.getUser(player.getUniqueId());
                    if (user == null) continue;

                    long seconds = user.getFlySeconds() - 1;
                    
                    // Send warning message at specific intervals
                    if (announces.contains(seconds)) {
                        message(player, Message.COMMAND_FLY_REMAINING, "%time%", TimerBuilder.getStringTime(seconds * 1000));
                    }

                    // Disable flight when time expires
                    if (seconds <= 0) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        message(player, Message.COMMAND_FLY_END);
                    }
                    user.setFlySeconds(seconds);
                }
            }
            
            // Clear processed batch - Memory optimization
            currentBatch.clear();

        }, 1, 1, TimeUnit.SECONDS);
    }

}
