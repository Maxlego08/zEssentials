package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;

public class CommandEnchant extends VCommand {
    public CommandEnchant(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ENCHANT);
        this.setDescription(Message.DESCRIPTION_ENCHANT);
        this.addRequireArg("enchantment", (a, b) -> plugin.getEnchantments().getEnchantments());
        this.addRequireArg("level", (a, b) -> Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String enchantAsString = this.argAsString(0);
        int level = this.argAsInteger(1);
        Player player = this.argAsPlayer(2, this.player);

        if (level < 0) return CommandResultType.SYNTAX_ERROR;

        if (player == null) {
            message(sender, Message.COMMAND_SPEED_INVALID);
            return CommandResultType.DEFAULT;
        }

        var optional = plugin.getEnchantments().getEnchantments(enchantAsString);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ENCHANT_ERROR_ENCHANT, "%enchant%", enchantAsString);
            return CommandResultType.DEFAULT;
        }

        Enchantment enchantment = optional.get().enchantment();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, player, Message.COMMAND_ENCHANT_ERROR_ITEM_SELF, Message.COMMAND_ENCHANT_ERROR_ITEM_PLAYER);
            return CommandResultType.DEFAULT;
        }

        enchant(itemStack, enchantment, level);
        String translatedEnchantments = "<lang:" + enchantment.translationKey() + ">";

        if (level == 0) {
            message(sender, player, Message.COMMAND_ENCHANT_REMOVE_SELF, Message.COMMAND_ENCHANT_REMOVE_PLAYER, "%enchant%", translatedEnchantments);
        } else {
            message(sender, player, Message.COMMAND_ENCHANT_SUCCESS_SELF, Message.COMMAND_ENCHANT_SUCCESS_PLAYER, "%enchant%", translatedEnchantments);
        }

        return CommandResultType.SUCCESS;
    }

    private void message(CommandSender sender, Player player, Message messageSelf, Message messagePlayer, Object... objects) {

        int newSize = objects.length + 2;
        Object[] newObjects = new Object[newSize];
        System.arraycopy(objects, 0, newObjects, 0, objects.length);
        newObjects[objects.length] = "%player%";
        newObjects[objects.length + 1] = player.getName();

        message(sender, sender == player ? messageSelf : messagePlayer, newObjects);
    }

    private void enchant(ItemStack itemStack, Enchantment enchantment, int level) {
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            var enchantmentStorageMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            if (level == 0) enchantmentStorageMeta.removeStoredEnchant(enchantment);
            else enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
            itemStack.setItemMeta(enchantmentStorageMeta);
        } else {
            if (level == 0) itemStack.removeEnchantment(enchantment);
            else itemStack.addUnsafeEnchantment(enchantment, level);
        }
    }
}
