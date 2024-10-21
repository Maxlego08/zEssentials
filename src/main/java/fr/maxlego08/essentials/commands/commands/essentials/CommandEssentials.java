package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandEssentials extends VCommand {

    public CommandEssentials(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_USE);
        this.addSubCommand(new CommandEssentialsReload(plugin));
        this.addSubCommand(new CommandEssentialsConvert(plugin));
        this.addSubCommand(new CommandEssentialsDeleteWorld(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        message(sender, Message.COMMAND_ESSENTIALS, "%version%", plugin.getDescription().getVersion());

        return CommandResultType.SUCCESS;
    }
}
