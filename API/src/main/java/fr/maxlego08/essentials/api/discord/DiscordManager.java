package fr.maxlego08.essentials.api.discord;

import fr.maxlego08.essentials.api.user.User;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

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

    /**
     * Links a user to a Discord account using a given code.
     *
     * @param user the user to link
     * @param code the code to use for linking
     */
    void linkAccount(User user, String code);

    /**
     * Sends a message to a Discord channel, using the given configuration and arguments.
     *
     * @param player        the player to send the message for
     * @param configuration the configuration for the message
     * @param args          the arguments to replace placeholders in the configuration
     */
    void sendDiscord(Player player, DiscordConfiguration configuration, String... args);

    /**
     * Loads a Discord configuration from the given configuration section and
     * applies it to the given consumer.
     *
     * @param configurationSection the configuration section to load from
     * @param consumer             the consumer to apply the loaded configuration to
     */
    void loadConfiguration(ConfigurationSection configurationSection, Consumer<DiscordConfiguration> consumer);
}
