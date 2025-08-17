package fr.maxlego08.essentials.api.utils.component;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface AdventureComponent extends ComponentMessage {

    /**
     * Creates a component from the given string.
     * This method is syntactic sugar for calling
     * {@link #getComponent(String, TagResolver)} with an empty
     * {@link TagResolver}.
     *
     * @param string The string to create a component from
     * @return The component
     */
    Component getComponent(String string);

    /**
     * Creates a component from the given string and tag resolver.
     * This method allows for the parsing of tags within the string,
     * using the provided tag resolver to replace or resolve any tags present.
     *
     * @param string      The string to create a component from
     * @param tagResolver The tag resolver to use for resolving tags in the string
     * @return The component generated from the string and tag resolver
     */
    Component getComponent(String string, TagResolver tagResolver);

    /**
     * Creates a boss bar from the given message and properties.
     *
     * @param message  The message to display in the boss bar
     * @param barColor The color of the boss bar
     * @param barStyle The style of the boss bar
     * @return The created boss bar
     */
    BossBar createBossBar(String message, BossBar.Color barColor, BossBar.Overlay barStyle);
}
