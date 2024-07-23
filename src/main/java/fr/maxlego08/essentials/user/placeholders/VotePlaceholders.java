package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

public class VotePlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {
        var iStorage = plugin.getStorageManager().getStorage();

        placeholder.register("voteparty_amount", (player) -> String.valueOf(plugin.getVoteManager().getCurrentVotePartyAmount()), "Returns the progress of the vote party");
        placeholder.register("voteparty_objective", (player) -> String.valueOf(plugin.getVoteManager().getVotePartyObjective()), "Returns the objective of the vote party");

        placeholder.register("vote_amount", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user == null ? "0" : String.valueOf(user.getVote());
        }, "Returns the player vote amount");
    }

}
