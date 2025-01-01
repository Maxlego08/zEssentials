package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandEssentialsClearRandomWord extends VCommand {

    public CommandEssentialsClearRandomWord(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("clear-random-word");
        this.setPermission(Permission.ESSENTIALS_CLEAR_RANDOM_WORD);
        this.setDescription(Message.DESCRIPTION_RELOAD);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getRandomWord().clear();
        message(sender, Message.RANDOM_WORD_CLEAR);

        return CommandResultType.SUCCESS;
    }
}
