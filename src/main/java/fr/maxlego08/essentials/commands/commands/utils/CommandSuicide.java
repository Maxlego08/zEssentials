package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.RuleModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.attribute.Attribute;

public class CommandSuicide extends VCommand {
    public CommandSuicide(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(RuleModule.class);
        this.setDescription(Message.DESCRIPTION_SUICIDE);
        this.setPermission(Permission.ESSENTIALS_SUICIDE);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        var attribute = this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return CommandResultType.SYNTAX_ERROR;
        this.player.damage(attribute.getBaseValue() * 2);

        return CommandResultType.SUCCESS;
    }
}
