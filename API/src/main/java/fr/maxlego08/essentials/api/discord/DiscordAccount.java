package fr.maxlego08.essentials.api.discord;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a discord account linked to a minecraft account.
 */
public interface DiscordAccount {

    /**
     * Gets the user ID of the discord user.
     *
     * @return the user ID of the discord user
     */
    long getUserId();

    /**
     * Gets the UUID of the minecraft user.
     *
     * @return the UUID of the minecraft user
     */
    UUID getMinecraftId();

    /**
     * Gets the name of the discord user.
     *
     * @return the name of the discord user
     */
    String getDiscordName();

    /**
     * Gets the name of the minecraft user.
     *
     * @return the name of the minecraft user
     */
    String getMinecraftName();

    /**
     * Gets the date where the account was created.
     *
     * @return the date where the account was created
     */
    Date getCreatedAt();

}
