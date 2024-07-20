package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.inventory.ItemStack;

public class CommandHat extends VCommand {
    public CommandHat(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_HAT);
        this.setDescription(Message.DESCRIPTION_HAT);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        ItemStack itemStack = player.getInventory().getHelmet();
        ItemStack inHand = player.getInventory().getItemInMainHand();

        if (inHand.getType().isAir()) {
            message(this.sender, Message.COMMAND_HAT_ERROR);
            return CommandResultType.DEFAULT;
        }

        this.player.getInventory().setHelmet(inHand);
        this.player.getInventory().setItemInMainHand(itemStack);
        message(this.sender, Message.COMMAND_HAT_SUCCESS, "%item%", name(inHand.getType().name()));
        
        return CommandResultType.SUCCESS;
    }
}
