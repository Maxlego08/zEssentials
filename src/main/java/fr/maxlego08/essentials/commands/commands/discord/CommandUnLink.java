package fr.maxlego08.essentials.commands.commands.discord;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.discord.DiscordModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandUnLink extends VCommand {

    public CommandUnLink(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_DISCORD_UNLINK);
        this.setDescription(Message.DESCRIPTION_DISCORD_UNLINK);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getModuleManager().getModule(DiscordModule.class).unlinkAccount(this.user);

        return CommandResultType.SUCCESS;
    }
}
