package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Loadable;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportationModule extends ZModule {

    private final List<TeleportPermission> teleportDelayPermissions = new ArrayList<>();
    private boolean teleportSafety;
    private boolean teleportToCenter;
    private int teleportDelay;
    private int teleportTpaExpire;
    private boolean teleportDelayBypass;
    private boolean openConfirmInventoryForTpa;

    public TeleportationModule(ZEssentialsPlugin plugin) {
        super(plugin, "teleportation");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("confirm_request_inventory");
    }

    public boolean isTeleportSafety() {
        return teleportSafety;
    }

    public boolean isTeleportToCenter() {
        return teleportToCenter;
    }

    public int getTeleportDelay() {
        return teleportDelay;
    }

    public boolean isTeleportDelayBypass() {
        return teleportDelayBypass;
    }

    public List<TeleportPermission> getTeleportDelayPermissions() {
        return teleportDelayPermissions;
    }

    public int getTeleportTpaExpire() {
        return teleportTpaExpire;
    }

    public boolean isOpenConfirmInventoryForTpa() {
        return openConfirmInventoryForTpa;
    }

    public int getTeleportationDelay(Player player) {
        return this.teleportDelayPermissions.stream().filter(teleportPermission -> player.hasPermission(teleportPermission.permission)).mapToInt(TeleportPermission::delay).min().orElse(this.teleportDelay);
    }

    public void openConfirmInventory(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "confirm_request_inventory");
    }

    public record TeleportPermission(String permission, int delay) implements Loadable {

    }

}
