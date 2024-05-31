package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandRepairAll extends VCommand {
    public CommandRepairAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_REPAIR_ALL);
        this.setDescription(Message.DESCRIPTION_REPAIR_ALL);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {


        int amount = 0;

        for (ItemStack itemStack : this.player.getInventory().getContents()) {
            if (itemStack != null) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta instanceof Damageable damageable) {

                    damageable.setDamage(0);
                    itemStack.setItemMeta(itemMeta);

                    amount++;
                }
            }
        }

        if (amount == 0) {
            message(sender, Message.COMMAND_REPAIR_ALL_ERROR);
            return CommandResultType.DEFAULT;
        }

        this.player.updateInventory();
        message(sender, Message.COMMAND_REPAIR_ALL_SUCCESS);
        return CommandResultType.SUCCESS;
    }
}
