package fr.maxlego08.essentials.commands.commands.discord;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.discord.DiscordModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandLink extends VCommand {

    public CommandLink(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_DISCORD_LINK);
        this.setDescription(Message.DESCRIPTION_DISCORD_LINK);
        this.onlyPlayers();
        this.addRequireArg("code");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String code = this.argAsString(0);
        plugin.getModuleManager().getModule(DiscordModule.class).linkAccount(this.user, code);

        return CommandResultType.SUCCESS;
    }
}
