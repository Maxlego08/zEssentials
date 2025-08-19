package fr.maxlego08.essentials.listener;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.event.events.user.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.DynamicCooldown;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import java.util.Optional;

public class PlayerListener extends ZUtils implements Listener {

    private final EssentialsPlugin plugin;
    private final DynamicCooldown dynamicCooldown = new DynamicCooldown();
    private final Set<UUID> disabledFly = new HashSet<>();

    public PlayerListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    private User getUser(Entity player) {
        return this.plugin.getStorageManager().getStorage().getUser(player.getUniqueId());
    }

    private void cancelGoldEvent(Player player, Cancellable event) {
        User user = getUser(player);
        if (user != null && user.getOption(Option.GOD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            cancelGoldEvent(player, event);

            var user = getUser(player);
            if (user == null) return;

            if (user.getProtectionDuration() > System.currentTimeMillis()) {
                event.setCancelled(true);
            }
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
        if (user != null && user.getOption(Option.INVSEE) && !user.hasPermission(Permission.ESSENTIALS_INVSEE_INTERACT)) {
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

        if (user.hasPermission(Permission.ESSENTIALS_BACK_DEATH)) {
            user.setLastLocation();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        User user = getUser(event.getPlayer());
        if (user == null) return;

        user.setLastLocation();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        Configuration configuration = this.plugin.getConfiguration();
        Player player = event.getPlayer();

        String label = event.getMessage().substring(1).split(" ")[0];
        for (var restriction : configuration.getCommandRestrictions()) {
            if (restriction.command().equalsIgnoreCase(label)) {
                String bypass = restriction.bypassPermission();
                if (bypass != null && !bypass.isEmpty() && player.hasPermission(bypass)) {
                    continue;
                }
                if (restriction.worlds() != null && restriction.worlds().contains(player.getWorld().getName())) {
                    message(player, Message.COMMAND_RESTRICTED);
                    event.setCancelled(true);
                    return;
                }
                if (restriction.cuboids() != null) {
                    var location = player.getLocation();
                    for (var cuboid : restriction.cuboids()) {
                        if (cuboid.contains(location)) {
                            message(player, Message.COMMAND_RESTRICTED);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        long[] cooldownsArray = configuration.getCooldownCommands();
        if (cooldownsArray.length == 0) return;
        double cooldown = handleCooldown(player, cooldownsArray);
        if (cooldown != 0 && !hasPermission(player, Permission.ESSENTIALS_COOLDOWN_COMMAND_BYPASS)) {
            message(player, Message.COOLDOWN_COMMANDS, "%cooldown%", TimerBuilder.getStringTime(cooldown));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandHighest(PlayerCommandPreprocessEvent event) {

        Configuration configuration = this.plugin.getConfiguration();
        if (configuration.isEnableCommandLog()) {
            this.plugin.getStorageManager().getStorage().insertCommand(event.getPlayer().getUniqueId(), event.getMessage());
        }
    }

    private double handleCooldown(Player player, long[] cooldownsArray) {

        long wait;

        synchronized (this.dynamicCooldown) {
            wait = this.dynamicCooldown.limited(player.getUniqueId(), cooldownsArray);
            if (wait == 0L) this.dynamicCooldown.add(player.getUniqueId());
        }

        return wait != 0L ? wait : 0.0;
    }

    @EventHandler
    public void onFirstJoin(UserFirstJoinEvent event) {
        var user = event.getUser();
        this.plugin.getConfiguration().getDefaultOptionValues().forEach(user::setOption);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        var player = event.getPlayer();
        User user = this.plugin.getUser(player.getUniqueId());
        if (user != null) user.startCurrentSessionPlayTime();

        if (user != null && user.isFirstJoin() && ConfigStorage.spawnLocation != null && ConfigStorage.spawnLocation.isValid()) {
            this.plugin.getScheduler().teleportAsync(player, ConfigStorage.spawnLocation.getLocation());
        }

        this.plugin.getScheduler().runAtLocationLater(player.getLocation(), () -> {

            if (hasPermission(player, Permission.ESSENTIALS_FLY_SAFELOGIN) && shouldFlyBasedOnLocation(player.getLocation())) {
                player.setAllowFlight(true);
                player.setFlying(true);
            }

            if (!hasPermission(player, Permission.ESSENTIALS_SPEED)) {
                player.setFlySpeed(0.1f);
                player.setWalkSpeed(0.2f);
            }
        }, 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        User user = this.plugin.getUser(event.getPlayer().getUniqueId());
        if (user == null) return;
        long sessionPlayTime = (System.currentTimeMillis() - user.getCurrentSessionPlayTime()) / 1000;
        long playtime = user.getPlayTime();
        this.plugin.getStorageManager().getStorage().insertPlayTime(user.getUniqueId(), sessionPlayTime, playtime, user.getAddress());
        this.disabledFly.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();

        User user = this.plugin.getUser(event.getPlayer().getUniqueId());
        if (user == null || user.getOption(Option.POWER_TOOLS_DISABLE)) return;

        Optional<String> optional = user.getPowerTool(itemStack.getType());
        if (optional.isEmpty()) return;

        String command = optional.get();
        event.setCancelled(event.getPlayer().performCommand(command));
    }

    @EventHandler
    public void onInteract(PlayerItemConsumeEvent event) {

        if (event.isCancelled()) return;

        var player = event.getPlayer();
        User user = this.plugin.getUser(player.getUniqueId());

        if (user != null && user.getOption(Option.NIGHT_VISION) && event.getItem().getType() == Material.MILK_BUCKET) {
            this.plugin.getScheduler().runAtLocationLater(player.getLocation(), wrappedTask -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 1, false, false, false), true), 2);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        var player = event.getPlayer();
        User user = this.plugin.getUser(player.getUniqueId());

        if (user != null && user.getOption(Option.NIGHT_VISION)) {
            this.plugin.getScheduler().runAtLocationLater(player.getLocation(), wrappedTask -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 1, false, false, false), true), 2);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPhantomSpawn(EntityTargetEvent event) {
        var target = event.getTarget();
        var entity = event.getEntity();
        if (entity instanceof Phantom && target instanceof Player player) {
            User user = this.plugin.getUser(player.getUniqueId());
            if (user != null && user.getOption(Option.PHANTOMS_DISABLE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChangeWorld(PlayerChangedWorldEvent event) {

        var player = event.getPlayer();
        var configuration = plugin.getConfiguration();
        var worldName = player.getWorld().getName();

        if (configuration.getDisableFlyWorld().contains(worldName) && player.isFlying() && !hasPermission(player, Permission.ESSENTIALS_FLY_BYPASS_WORLD)) {
            player.setAllowFlight(false);
            player.setFlying(false);
            this.disabledFly.add(player.getUniqueId());
            message(player, Message.COMMAND_FLY_ERROR_WORLD);
        } else if (configuration.isEnableFlyReturn() && !configuration.getDisableFlyWorld().contains(worldName) && this.disabledFly.remove(player.getUniqueId())) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        User user = this.plugin.getUser(player.getUniqueId());
        if (user != null && hasPermission(player, Permission.ESSENTIALS_SIGN_COLOR)) {
            this.plugin.getComponentMessage().changeSignColor(event);
        }
    }
}
