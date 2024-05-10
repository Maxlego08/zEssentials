package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CommandPowerTools extends VCommand {

    public CommandPowerTools(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_POWER_TOOLS);
        this.setDescription(Message.DESCRIPTION_POWER_TOOLS);
        this.addOptionalArg("command");
        this.setExtendedArgs(true);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String command = this.getArgs(0);
        ItemStack itemStack = this.player.getInventory().getItemInMainHand();

        Material material = itemStack.getType();
        if (material.isAir()) {
            message(sender, Message.COMMAND_POWER_TOOL_ERROR_ITEM);
            return CommandResultType.DEFAULT;
        }

        if (command.isEmpty()) {

            if (this.user.getPowerTool(material).isEmpty()) {
                message(sender, Message.COMMAND_POWER_TOOL_ERROR_RESET);
                return CommandResultType.DEFAULT;
            }

            this.user.deletePowerTools(material);
            message(sender, Message.COMMAND_POWER_TOOL_RESET, "%item%", material);

            return CommandResultType.DEFAULT;
        }

        this.user.setPowerTools(material, command);
        message(sender, Message.COMMAND_POWER_TOOL_INFO, "%item%", material, "%command%", command);

        return CommandResultType.SUCCESS;
    }
}
