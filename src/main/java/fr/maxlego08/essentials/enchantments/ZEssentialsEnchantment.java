package fr.maxlego08.essentials.enchantments;

import fr.maxlego08.essentials.api.enchantment.EssentialsEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public record ZEssentialsEnchantment(Enchantment enchantment, List<String> aliases) implements EssentialsEnchantment {

}
