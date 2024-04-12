package fr.maxlego08.essentials.listener;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

    private final EssentialsPlugin plugin;

    public PlayerListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    private User getUser(Entity player) {
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

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        User user = getUser(event.getWhoClicked());
        if (user.getOption(Option.INVSEE) && !user.hasPermission(Permission.ESSENTIALS_INVSEE_INTERACT)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        User user = getUser(event.getPlayer());
        if (user != null && user.getOption(Option.INVSEE)) user.setOption(Option.INVSEE, false);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        User user = getUser(event.getPlayer());
        if (user == null) return;

        user.setLastLocation();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        User user = getUser(event.getPlayer());
        System.out.println("Je me téléporte ici ! " + event.getPlayer());
        if (user == null) return;

        System.out.println("Oui ?");
        user.setLastLocation();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(EntityTeleportEvent event) {
        User user = getUser(event.getEntity());
        System.out.println("Je me téléporte ici 22 ! " + event.getEntity());
        if (user == null) return;

        System.out.println("Oui ?");
        user.setLastLocation();
    }

}
