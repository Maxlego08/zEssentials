package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandEssentialsReload extends VCommand {

    public CommandEssentialsReload(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("reload", "rl");
        this.setPermission(Permission.ESSENTIALS_RELOAD);
        this.setDescription(Message.DESCRIPTION_RELOAD);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getConfigurationFiles().forEach(ConfigurationFile::load);
        message(sender, Message.COMMAND_RELOAD);

        return CommandResultType.SUCCESS;
    }
}
