package fr.maxlego08.essentials.module.modules.kit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permissible;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ZKit extends ZUtils implements Kit {

    private final EssentialsPlugin plugin;
    private final String displayName;
    private final String name;
    private final String permission;
    private final long cooldown;
    private final Map<String, Long> permissionCooldowns;
    private final List<Action> actions;
    private final File file;
    private List<MenuItemStack> menuItemStacks;

    private MenuItemStack helmet;
    private MenuItemStack chestplate;
    private MenuItemStack leggings;
    private MenuItemStack boots;

    public ZKit(EssentialsPlugin plugin, String displayName, String name, long cooldown, Map<String, Long> permissionCooldowns, List<MenuItemStack> menuItemStacks, List<Action> actions, String permission, File file) {
        this.plugin = plugin;
        this.displayName = displayName;
        this.name = name;
        this.cooldown = cooldown;
        this.permissionCooldowns = permissionCooldowns;
        this.menuItemStacks = menuItemStacks;
        this.actions = actions;
        this.permission = permission;
        this.file = file;
    }

    @Override
    public long getCooldown(Permissible permissible) {
        long currentCooldown = this.cooldown;
        for (Map.Entry<String, Long> entry : this.permissionCooldowns.entrySet()) {
            if (permissible.hasPermission(entry.getKey())) {
                currentCooldown = Math.min(currentCooldown, entry.getValue());
            }
        }
        return currentCooldown;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Map<String, Long> getPermissionCooldowns() {
        return permissionCooldowns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public List<MenuItemStack> getMenuItemStacks() {
        return menuItemStacks;
    }

    public void setMenuItemStacks(List<MenuItemStack> menuItemStacks) {
        this.menuItemStacks = menuItemStacks;
    }

    @Override
    public void give(Player player) {

        this.menuItemStacks.forEach(menuItemStack -> this.plugin.give(player, menuItemStack.build(player, false)));

        var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();
        actions.forEach(action -> action.preExecute(player, null, fakeInventory, new Placeholders()));

        this.equip(player, helmet, EquipmentSlot.HEAD);
        this.equip(player, chestplate, EquipmentSlot.CHEST);
        this.equip(player, leggings, EquipmentSlot.LEGS);
        this.equip(player, boots, EquipmentSlot.FEET);
    }

    private void equip(Player player, MenuItemStack menuItemStack, EquipmentSlot equipmentSlot) {
        if (menuItemStack == null) return;

        var itemStack = menuItemStack.build(player, false);
        var inventory = player.getInventory();
        if (inventory.getItem(equipmentSlot).getType().isAir()) {
            inventory.setItem(equipmentSlot, itemStack);
        } else {
            plugin.give(player, itemStack);
        }
    }

    @Override
    public void setItems(List<MenuItemStack> menuItemStacks) {
        this.menuItemStacks = menuItemStacks;
    }

    @Override
    public List<Action> getActions() {
        return actions;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(this.permission);
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public MenuItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(MenuItemStack helmet) {
        this.helmet = helmet;
    }

    @Override
    public MenuItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(MenuItemStack chestplate) {
        this.chestplate = chestplate;
    }

    @Override
    public MenuItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(MenuItemStack leggings) {
        this.leggings = leggings;
    }

    @Override
    public MenuItemStack getBoots() {
        return boots;
    }

    public void setBoots(MenuItemStack boots) {
        this.boots = boots;
    }
}
