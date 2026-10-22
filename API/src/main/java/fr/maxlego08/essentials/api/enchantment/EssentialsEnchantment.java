package fr.maxlego08.essentials.api.enchantment;

import org.bukkit.enchantments.Enchantment;

import java.util.List;

/**
 * Legacy wrapper for a registered enchantment.
 *
 * @deprecated Use {@link Enchantment} and {@link Enchantments#getEnchantment(String)} directly.
 */
@Deprecated
public interface EssentialsEnchantment {

    /**
     * Retrieves the underlying Bukkit enchantment.
     *
     * @return the enchantment
     */
    Enchantment enchantment();

    /**
     * Retrieves the identifiers associated with this wrapper.
     * Wrappers returned by zEssentials contain only the canonical identifier;
     * the legacy enchantment aliases are no longer supported.
     *
     * @return the enchantment identifiers
     */
    List<String> aliases();
}
