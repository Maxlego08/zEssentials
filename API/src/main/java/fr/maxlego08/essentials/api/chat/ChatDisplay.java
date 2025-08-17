package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public interface ChatDisplay {

    /**
     * Displays a formatted chat message between the sender and receiver using the specified
     * adventure component and tag resolver.
     *
     * @param adventureComponent The component that defines the adventure text format.
     * @param builder The builder for resolving tags within the message.
     * @param sender The player sending the message.
     * @param receiver The player receiving the message.
     * @param message The raw message content to be formatted and displayed.
     * @return A string representing the formatted chat message.
     */
    String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message);

    /**
     * Checks if the given permissible entity has the necessary permissions to view
     * chat messages.
     *
     * @param permissible The permissible entity to check.
     * @return true if the entity has the necessary permissions, false otherwise.
     */
    boolean hasPermission(Permissible permissible);
}
