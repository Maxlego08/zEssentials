package fr.maxlego08.essentials.hooks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class NuVotifierHook implements Listener {

    private final EssentialsPlugin plugin;

    public NuVotifierHook(EssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        var offlinePlayer = Bukkit.getOfflinePlayer(vote.getUsername());
        this.plugin.getVoteManager().addPlayerVote(offlinePlayer, vote.getServiceName());
    }
}
