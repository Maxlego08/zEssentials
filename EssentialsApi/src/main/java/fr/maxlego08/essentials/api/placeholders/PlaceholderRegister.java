package fr.maxlego08.essentials.api.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;

/**
 * Represents a placeholder register for registering placeholder systems with plugins.
 */
public interface PlaceholderRegister {

    /**
     * Registers a placeholder system with the specified plugin.
     *
     * @param placeholder The placeholder system to register.
     * @param plugin      The plugin to register the placeholder system with.
     */
    void register(Placeholder placeholder, EssentialsPlugin plugin);

}
