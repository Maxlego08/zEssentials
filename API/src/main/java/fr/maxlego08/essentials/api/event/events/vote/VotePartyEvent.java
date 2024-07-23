package fr.maxlego08.essentials.api.event.events.vote;

import fr.maxlego08.essentials.api.event.CancellableEssentialsEvent;

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
