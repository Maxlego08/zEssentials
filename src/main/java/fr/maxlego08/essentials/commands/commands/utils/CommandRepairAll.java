package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class CommandRepairAll extends VCommand {
    public CommandRepairAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_REPAIR_ALL);
        this.setDescription(Message.DESCRIPTION_REPAIR_ALL);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (!hasPermission(sender, Permission.ESSENTIALS_REPAIR_ALL_OTHER)) {
            player = this.player;
        }

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        int amount = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta instanceof Damageable damageable) {

                    PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
                    if (damageable.getDamage() == 0 || persistentDataContainer.has(CommandRepair.UNREPAIRABLE_KEY)) {
                        continue;
                    }

                    damageable.setDamage(0);
                    itemStack.setItemMeta(itemMeta);

                    amount++;
                }
            }
        }

        if (amount == 0) {
            message(player, Message.COMMAND_REPAIR_ALL_ERROR);
            return CommandResultType.DEFAULT;
        }

        player.updateInventory();
        message(player, Message.COMMAND_REPAIR_ALL_SUCCESS);
        return CommandResultType.SUCCESS;
    }
}
