package fr.maxlego08.essentials.api.enchantment;

import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Optional;

/**
 * Provides access to enchantments registered on the server, including datapack and plugin enchantments.
 */
public interface Enchantments {

    /**
     * Retrieves an enchantment by its namespaced key.
     * Keys without a namespace use the Minecraft namespace.
     *
     * @param enchantment the key of the enchantment
     * @return an {@link Optional} containing the enchantment if found, otherwise empty
     */
    default Optional<Enchantment> getEnchantment(String enchantment) {
        return getEnchantments(enchantment).map(EssentialsEnchantment::enchantment);
    }

    /**
     * Retrieves an enchantment using the legacy wrapper.
     *
     * @param enchantment the key of the enchantment
     * @return an {@link Optional} containing the wrapper if found, otherwise empty
     * @deprecated Use {@link #getEnchantment(String)} to retrieve the Bukkit enchantment directly.
     */
    @Deprecated
    Optional<EssentialsEnchantment> getEnchantments(String enchantment);

    /**
     * Retained for compatibility; zEssentials reads enchantments directly from the server registry.
     *
     * @deprecated Registration is no longer required. This method has no effect in zEssentials.
     */
    @Deprecated
    void register();

    /**
     * Retrieves the identifiers of all enchantments registered on the server.
     * The Minecraft namespace is omitted; custom namespaces are preserved.
     *
     * @return a sorted list of enchantment identifiers
     */
    List<String> getEnchantments();
}
