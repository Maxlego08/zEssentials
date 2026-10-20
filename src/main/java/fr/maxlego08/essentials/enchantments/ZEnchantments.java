package fr.maxlego08.essentials.enchantments;

import fr.maxlego08.essentials.api.enchantment.Enchantments;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ZEnchantments implements Enchantments {

    @Override
    public Optional<Enchantment> getEnchantments(String enchantment) {
        NamespacedKey key = NamespacedKey.fromString(enchantment.toLowerCase(Locale.ROOT));
        if (key == null) return Optional.empty();

        return Optional.ofNullable(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key));
    }

    @Override
    public List<String> getEnchantments() {
        var registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        return registry.stream().map(registry::getKeyOrThrow)
                .map(key -> key.getNamespace().equals(NamespacedKey.MINECRAFT) ? key.getKey() : key.toString())
                .sorted().toList();
    }

}
