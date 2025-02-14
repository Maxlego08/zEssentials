package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class CommandRepair extends VCommand {

    public static final NamespacedKey UNREPAIRABLE_KEY = new NamespacedKey("zitems", "unrepairable");

    public CommandRepair(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_REPAIR);
        this.setDescription(Message.DESCRIPTION_REPAIR);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if (!itemStack.hasItemMeta()) {
            message(sender, Message.COMMAND_REPAIR_ERROR);
            return CommandResultType.DEFAULT;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta instanceof Damageable damageable) {

            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            if (damageable.getDamage() == 0 || persistentDataContainer.has(UNREPAIRABLE_KEY)) {
                message(sender, Message.COMMAND_REPAIR_ERROR);
                return CommandResultType.DEFAULT;
            }

            damageable.setDamage(0);
            itemStack.setItemMeta(itemMeta);
            this.player.updateInventory();

            message(sender, Message.COMMAND_REPAIR_SUCCESS);
            return CommandResultType.SUCCESS;
        }

        message(sender, Message.COMMAND_REPAIR_ERROR);
        return CommandResultType.DEFAULT;
    }
}
