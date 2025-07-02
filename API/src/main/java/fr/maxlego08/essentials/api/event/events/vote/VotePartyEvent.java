package fr.maxlego08.essentials.api.event.events.vote;

import fr.maxlego08.essentials.api.event.CancellableEssentialsEvent;

/**
 * This event is triggered when a vote party is updating
 * You can get the vote party amount (the amount of vote needed to start the party)
 * You can also cancel the party
 */
public class VotePartyEvent extends CancellableEssentialsEvent {

    private long votePartyAmount;

    public VotePartyEvent(long votePartyAmount) {
        this.votePartyAmount = votePartyAmount;
    }

    public long getVotePartyAmount() {
        return votePartyAmount;
    }

    public void setVotePartyAmount(long votePartyAmount) {
        this.votePartyAmount = votePartyAmount;
    }
}
