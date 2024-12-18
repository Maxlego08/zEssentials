package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserItemsPlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        placeholder.register("iteminhand_maxdurability", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            return String.valueOf(itemStack.getType().getMaxDurability());
        }, "Returns the maximum durability of the item in hand");

        placeholder.register("iteminhand_custommodeldata", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (!itemStack.hasItemMeta()) return "0";
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            return meta != null && meta.hasCustomModelData() ? String.valueOf(meta.getCustomModelData()) : "0";
        }, "Returns the custom model data of the item in hand");

        placeholder.register("iteminhand_displayname", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().toString();
        }, "Returns the display name of the item in hand");

        placeholder.register("iteminhand_realname", player -> player.getInventory().getItemInMainHand().getType().getKey().getKey(), "Returns the formatted material name of the item in hand");
        placeholder.register("iteminhand_type", player -> player.getInventory().getItemInMainHand().getType().name(), "Returns the material name of the item in hand");

        placeholder.register("iteminhand_amount", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            return String.valueOf(itemStack.getAmount());
        }, "Returns the amount of items in the main hand");

        placeholder.register("iteminhand_durability", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof Damageable damageableItem) {
                return String.valueOf(damageableItem.getDamage());
            }
            return "0";
        }, "Returns the amount of durability left of the item in hand");

        placeholder.register("iteminhand_lore", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
                return String.join("\n", itemStack.getItemMeta().getLore());
            }
            return "";
        }, "Returns the lore of the item in hand");

        placeholder.register("iteminhand_enchantments", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) {
                List<String> enchantments = new ArrayList<>();
                for (Map.Entry<Enchantment, Integer> entry : itemStack.getItemMeta().getEnchants().entrySet()) {
                    enchantments.add(entry.getKey().getKey().getKey() + ":" + entry.getValue());
                }
                return String.join(", ", enchantments);
            }
            return "";
        }, "Returns the enchantments of the item in hand with their level");

        placeholder.register("iteminhand_hasenchantment_", (player, enchantmentName) -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) {
                var optional = plugin.getEnchantments().getEnchantments(enchantmentName);
                if (optional.isPresent()) {
                    var essentialsEnchantment = optional.get();
                    return itemStack.containsEnchantment(essentialsEnchantment.enchantment()) ? "true" : "false";
                }
            }
            return "false";
        }, "Returns true if the item in hand has at least one enchantment", "enchantment");

        placeholder.register("iteminhand_enchantmentlevel_", (player, enchantmentName) -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) {
                var optional = plugin.getEnchantments().getEnchantments(enchantmentName);
                if (optional.isPresent()) {
                    var essentialsEnchantment = optional.get();
                    return String.valueOf(itemStack.getEnchantmentLevel(essentialsEnchantment.enchantment()));
                }
            }
            return "0";
        }, "Returns the level of a specific enchantment on the item in hand", "enchantment");

        placeholder.register("iteminhand_itemflags", (player) -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && !itemStack.getItemMeta().getItemFlags().isEmpty()) {
                return itemStack.getItemMeta().getItemFlags().stream().map(ItemFlag::name).collect(Collectors.joining(", "));
            }
            return "";
        }, "Returns the itemflags of the item in hand");

        placeholder.register("iteminhand_hasitemflag_", (player, itemFlagName) -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta()) {
                return itemStack.getItemMeta().hasItemFlag(ItemFlag.valueOf(itemFlagName)) ? "true" : "false";
            }
            return "false";
        }, "Returns true if the item in hand has a specific itemflag", "item flag");

    }
}
