package fr.maxlego08.essentials.commands.commands.vote;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.vote.VoteSiteConfiguration;
import fr.maxlego08.essentials.module.modules.VoteModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;

public class CommandVoteAdd extends VCommand {

    public CommandVoteAdd(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VoteModule.class);
        this.addSubCommand("add");
        this.setPermission(Permission.ESSENTIALS_VOTE_ADD);
        this.setDescription(Message.DESCRIPTION_VOTE_ADD);
        this.addRequirePlayerNameArg();
        this.addRequireArg("site", (a, b) -> plugin.getVoteManager().getSites().stream().map(VoteSiteConfiguration::name).map(e -> e.replace(" ", "_")).toList());
        this.addOptionalArg("silent", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        String site = this.argAsString(1).replace("_", " ");
        var voteManager = plugin.getVoteManager();

        if (!voteManager.siteExist(site)) {
            message(sender, Message.COMMAND_VOTE_ADD_ERROR, "%site%", site);
            return CommandResultType.DEFAULT;
        }

        boolean isSilent = this.argAsBoolean(2, false);
        voteManager.addPlayerVote(offlinePlayer, site);
        if (!isSilent) {
            message(sender, Message.COMMAND_VOTE_ADD, "%player%", offlinePlayer.getName());
        }

        return CommandResultType.SUCCESS;
    }
}
