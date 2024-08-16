package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.RuleModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandRules extends VCommand {
    public CommandRules(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(RuleModule.class);
        this.setDescription(Message.DESCRIPTION_RULES);
        this.setPermission(Permission.ESSENTIALS_RULES);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getModuleManager().getModule(RuleModule.class).sendRule(player);

        return CommandResultType.SUCCESS;
    }
}
