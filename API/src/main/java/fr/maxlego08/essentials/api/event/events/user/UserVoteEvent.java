package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.event.CancellableEssentialsEvent;

import java.util.UUID;

/**
 * This event is triggered when a user votes on a website.
 */
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
