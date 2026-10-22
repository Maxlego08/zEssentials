package fr.maxlego08.essentials.enchantments;

import fr.maxlego08.essentials.api.enchantment.Enchantments;
import fr.maxlego08.essentials.api.enchantment.EssentialsEnchantment;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ZEnchantments implements Enchantments {

    @Override
    public Optional<Enchantment> getEnchantment(String enchantment) {
        NamespacedKey key = NamespacedKey.fromString(enchantment.toLowerCase(Locale.ROOT));
        if (key == null) return Optional.empty();

        return Optional.ofNullable(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key));
    }

    @Override
    @Deprecated
    public Optional<EssentialsEnchantment> getEnchantments(String enchantment) {
        return getEnchantment(enchantment).map(value -> {
            NamespacedKey key = value.getKey();
            String name = key.getNamespace().equals(NamespacedKey.MINECRAFT) ? key.getKey() : key.toString();
            return new ZEssentialsEnchantment(value, List.of(name));
        });
    }

    @Override
    @Deprecated
    public void register() {
        // Enchantments are provided by the server registry; no manual registration is needed.
    }

    @Override
    public List<String> getEnchantments() {
        var registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        return registry.stream().map(registry::getKeyOrThrow)
                .map(key -> key.getNamespace().equals(NamespacedKey.MINECRAFT) ? key.getKey() : key.toString())
                .sorted().toList();
    }

}
