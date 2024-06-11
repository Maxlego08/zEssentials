package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.inventory.Inventory;

public class CommandTrash extends VCommand {
    public CommandTrash(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_TRASH);
        this.setDescription(Message.DESCRIPTION_TRASH);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Inventory inventory = componentMessage.createInventory(Message.TRASH.getMessageAsString(), configuration.getTrashSize(), null);
        this.player.openInventory(inventory);

        return CommandResultType.SUCCESS;
    }
}
