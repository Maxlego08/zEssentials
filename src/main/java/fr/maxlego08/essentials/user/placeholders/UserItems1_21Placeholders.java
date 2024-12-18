package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

public class UserItems1_21Placeholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        placeholder.register("iteminhand_rarity", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            return itemStack.hasItemMeta() ? itemStack.getItemMeta().getRarity().name() : ItemRarity.UNCOMMON.name();
        }, "Returns the rarity of the item in hand");

        placeholder.register("iteminhand_repaircost", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof Repairable repairable) {
                return String.valueOf(repairable.getRepairCost());
            }
            return "0";
        }, "Returns the repair cost of the item in hand");

        placeholder.register("iteminhand_maxstacksize", player -> String.valueOf(player.getInventory().getItemInMainHand().getMaxStackSize()), "Returns the max stack size of the item in hand");
        placeholder.register("iteminhand_hide_tooltip", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            return String.valueOf(itemStack.hasItemMeta() && itemStack.getItemMeta().isHideTooltip());
        }, "Returns true if the item in hand has its tooltip hidden");

        placeholder.register("iteminhand_glint", player -> String.valueOf(player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().getEnchantmentGlintOverride()), "Returns true if the item in hand has the glint enchantment");
        placeholder.register("iteminhand_fire_resistant", player -> String.valueOf(player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().isFireResistant()), "Returns true if the item in hand is fire resistant");
        placeholder.register("iteminhand_unbreakable", player -> String.valueOf(player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().isUnbreakable()), "Returns true if the item in hand is unbreakable");
        placeholder.register("iteminhand_hide_unbreakable", player -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            return String.valueOf(itemStack.hasItemMeta() && itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_UNBREAKABLE));
        }, "Returns true if the tooltip unbreakable of the item in hand is hidden");


    }
}
