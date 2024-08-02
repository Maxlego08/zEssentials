package fr.maxlego08.essentials.api.enchantment;

import java.util.List;
import java.util.Optional;

public interface Enchantments {

    Optional<EssentialsEnchantment> getEnchantments(String enchantment);

    void register();

    List<String> getEnchantments();

}
