package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class CommandHeal extends VCommand {
    public CommandHeal(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_HEAL);
        this.setDescription(Message.DESCRIPTION_HEAL);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (this.player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        player.setFoodLevel(20);

        if (player == sender) {

            message(sender, Message.COMMAND_HEAL_RECEIVER);
        } else {

            message(sender, Message.COMMAND_HEAL_SENDER, "%player%", player.getName());
            message(player, Message.COMMAND_HEAL_RECEIVER);
        }

        return CommandResultType.SUCCESS;
    }
}
