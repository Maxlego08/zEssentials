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
    Optional<Enchantment> getEnchantments(String enchantment);

    /**
     * Retrieves the identifiers of all enchantments registered on the server.
     * The Minecraft namespace is omitted; custom namespaces are preserved.
     *
     * @return a sorted list of enchantment identifiers
     */
    List<String> getEnchantments();
}
