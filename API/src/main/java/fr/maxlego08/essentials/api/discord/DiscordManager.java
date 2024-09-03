package fr.maxlego08.essentials.api.discord;

/**
 * Interface for managing Discord-related configurations and operations.
 * Provides methods to retrieve different types of Discord configurations.
 */
public interface DiscordManager {

    /**
     * Retrieves the configuration for chat integration with Discord.
     *
     * @return the Discord chat configuration
     */
    DiscordConfiguration getChatConfiguration();

    /**
     * Retrieves the configuration for when a player first joins the server.
     *
     * @return the Discord first join configuration
     */
    DiscordConfiguration getFirstJoinConfiguration();

    /**
     * Retrieves the configuration for when a player joins the server.
     *
     * @return the Discord join configuration
     */
    DiscordConfiguration getJoinConfiguration();

    /**
     * Retrieves the configuration for when a player leaves the server.
     *
     * @return the Discord left configuration
     */
    DiscordConfiguration getLeftConfiguration();

}
