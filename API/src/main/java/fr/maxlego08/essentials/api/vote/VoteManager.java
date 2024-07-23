package fr.maxlego08.essentials.api.vote;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface VoteManager {

    long getCurrentVotePartyAmount();

    void setCurrentVotePartyAmount(long amount);

    void addCurrentVotePartyAmount(long amount);

    void removeCurrentVotePartyAmount(long amount);

    void onPlayerVote(UUID uniqueId, String site);

    void addPlayerVote(OfflinePlayer offlinePlayer, String site);

    void handleVoteParty();

    long getVotePartyObjective();

    void openVoteInventory(Player player);

    void sendVoteParty(Player player);

    boolean siteExist(String site);

    List<VoteSiteConfiguration> getSites();
}
