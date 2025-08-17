package fr.maxlego08.essentials.api.event.events.discord;

import fr.maxlego08.essentials.api.discord.DiscordAccount;
import fr.maxlego08.essentials.api.event.CancellableUserEvent;
import fr.maxlego08.essentials.api.user.User;

/**
 * This event is triggered when a user's discord account is unlinked.
 */
public class DiscordUnlinkEvent extends CancellableUserEvent {

    private final DiscordAccount discordAccount;

    public DiscordUnlinkEvent(User user, DiscordAccount discordAccount) {
        super(user);
        this.discordAccount = discordAccount;
    }

    public DiscordAccount getDiscordAccount() {
        return discordAccount;
    }
}
