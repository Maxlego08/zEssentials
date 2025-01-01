package fr.maxlego08.essentials.api.event.events.discord;

import fr.maxlego08.essentials.api.discord.DiscordAccount;
import fr.maxlego08.essentials.api.event.CancellableUserEvent;
import fr.maxlego08.essentials.api.user.User;

public class DiscordLinkEvent extends CancellableUserEvent {

    private final DiscordAccount discordAccount;

    public DiscordLinkEvent(User user, DiscordAccount discordAccount) {
        super(user);
        this.discordAccount = discordAccount;
    }

    public DiscordAccount getDiscordAccount() {
        return discordAccount;
    }
}
