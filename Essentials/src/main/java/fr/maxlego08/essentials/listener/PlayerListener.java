package fr.maxlego08.essentials.listener;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerListener implements Listener {

    private final EssentialsPlugin plugin;

    public PlayerListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    private User getUser(Player player) {
        return this.plugin.getStorageManager().getStorage().getUser(player.getUniqueId());
    }

    private void cancelGoldEvent(Player player, Cancellable event) {
        User user = getUser(player);
        if (user.getOption(Option.GOD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            cancelGoldEvent(player, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFood(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            cancelGoldEvent(player, event);
        }
    }

}
