package fr.maxlego08.essentials.api.placeholders;

import fr.maxlego08.essentials.api.functionnals.ReturnBiConsumer;
import fr.maxlego08.essentials.api.functionnals.ReturnConsumer;
import org.bukkit.entity.Player;

/**
 * Represents a placeholder system for registering and handling placeholders in messages.
 */
public interface Placeholder {

    /**
     * Registers a placeholder with a specified start string, consumer, description, and optional arguments.
     *
     * @param startWith   The start string that identifies the placeholder.
     * @param biConsumer  The consumer function to handle the placeholder.
     * @param description The description of the placeholder.
     * @param args        Optional arguments for the placeholder.
     */
    void register(String startWith, ReturnBiConsumer<Player, String, String> biConsumer, String description, String... args);

    /**
     * Registers a placeholder with a specified start string, consumer, and description.
     *
     * @param startWith   The start string that identifies the placeholder.
     * @param biConsumer  The consumer function to handle the placeholder.
     * @param description The description of the placeholder.
     */
    void register(String startWith, ReturnConsumer<Player, String> biConsumer, String description);

    /**
     * Gets the prefix used for placeholders.
     *
     * @return The prefix used for placeholders.
     */
    String getPrefix();

    /**
     * Handles placeholder requests and returns the replacement value.
     *
     * @param player The player requesting the placeholder replacement.
     * @param params The parameters of the placeholder request.
     * @return The replacement value for the placeholder.
     */
    String onRequest(Player player, String params);
}

