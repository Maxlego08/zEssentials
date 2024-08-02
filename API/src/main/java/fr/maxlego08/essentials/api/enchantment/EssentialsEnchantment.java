package fr.maxlego08.essentials.api.enchantment;

import org.bukkit.enchantments.Enchantment;

import java.util.List;

public interface EssentialsEnchantment {

    Enchantment enchantment();

    List<String> aliases();

}
