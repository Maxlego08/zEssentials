package fr.maxlego08.essentials.api.event.events.vote;

import fr.maxlego08.essentials.api.event.CancellableEssentialsEvent;

import java.util.UUID;

public class UserVoteEvent extends CancellableEssentialsEvent {

    private final UUID uniqueId;
    private final String site;

    public UserVoteEvent(UUID uniqueId, String site) {
        this.uniqueId = uniqueId;
        this.site = site;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getSite() {
        return site;
    }
}
