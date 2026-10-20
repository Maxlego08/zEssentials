package fr.maxlego08.essentials.enchantments;

import fr.maxlego08.essentials.api.enchantment.Enchantments;
import fr.maxlego08.essentials.api.enchantment.EssentialsEnchantment;
import org.bukkit.NamespacedKey;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ZEnchantments implements Enchantments {

    private final List<EssentialsEnchantment> essentialsEnchantments = new ArrayList<>();

    @Override
    public Optional<EssentialsEnchantment> getEnchantments(String enchantment) {
        return essentialsEnchantments.stream().filter(essentialsEnchantment -> essentialsEnchantment.aliases().contains(enchantment.toLowerCase(Locale.ROOT))).findFirst();
    }

    @Override
    public void register() {
        this.register(Enchantment.SHARPNESS, "alldamage", "alldmg", "sharpness", "sharp", "dal");
        this.register(Enchantment.BANE_OF_ARTHROPODS, "ardmg", "baneofarthropods", "baneofarthropod", "arthropod", "dar");
        this.register(Enchantment.SMITE, "undeaddamage", "smite", "du");
        this.register(Enchantment.EFFICIENCY, "digspeed", "efficiency", "minespeed", "cutspeed", "ds", "eff");
        this.register(Enchantment.UNBREAKING, "durability", "dura", "unbreaking", "d");
        this.register(Enchantment.THORNS, "thorns", "highcrit", "thorn", "highercrit", "t");
        this.register(Enchantment.FIRE_ASPECT, "fireaspect", "fire", "meleefire", "meleeflame", "fa");
        this.register(Enchantment.KNOCKBACK, "knockback", "kback", "kb", "k");
        this.register(Enchantment.FORTUNE, "blockslootbonus", "fortune", "fort", "lbb");
        this.register(Enchantment.LOOTING, "mobslootbonus", "mobloot", "looting", "lbm");
        this.register(Enchantment.RESPIRATION, "oxygen", "respiration", "breathing", "breath", "o");
        this.register(Enchantment.PROTECTION, "protection", "prot", "protect", "p");
        this.register(Enchantment.BLAST_PROTECTION, "explosionsprotection", "explosionprotection", "expprot", "blastprotection", "bprotection", "bprotect", "blastprotect", "pe");
        this.register(Enchantment.FEATHER_FALLING, "fallprotection", "fallprot", "featherfall", "featherfalling", "pfa");
        this.register(Enchantment.FIRE_PROTECTION, "fireprotection", "flameprotection", "fireprotect", "flameprotect", "fireprot", "flameprot", "pf");
        this.register(Enchantment.PROJECTILE_PROTECTION, "projectileprotection", "projprot", "pp");
        this.register(Enchantment.SILK_TOUCH, "silktouch", "softtouch", "st");
        this.register(Enchantment.AQUA_AFFINITY, "waterworker", "aquaaffinity", "watermine", "ww");
        this.register(Enchantment.FLAME, "firearrow", "flame", "flamearrow", "af");
        this.register(Enchantment.POWER, "arrowdamage", "power", "arrowpower", "ad");
        this.register(Enchantment.PUNCH, "arrowknockback", "arrowkb", "punch", "arrowpunch", "ak");
        this.register(Enchantment.INFINITY, "infinitearrows", "infarrows", "infinity", "infinite", "unlimited", "unlimitedarrows", "ai");
        this.register(Enchantment.LUCK_OF_THE_SEA, "luck", "luckofsea", "luckofseas", "rodluck");
        this.register(Enchantment.LURE, "lure", "rodlure");
        this.register(Enchantment.DEPTH_STRIDER, "depthstrider", "depth", "strider");
        this.register(Enchantment.FROST_WALKER, "frostwalker", "frost", "walker");
        this.register(Enchantment.MENDING, "mending");
        this.register(Enchantment.BINDING_CURSE, "bindingcurse", "bindcurse", "binding", "bind");
        this.register(Enchantment.VANISHING_CURSE, "vanishingcurse", "vanishcurse", "vanishing", "vanish");
        this.register(Enchantment.SWEEPING_EDGE, "sweepingedge", "sweepedge", "sweeping");
        this.register(Enchantment.LOYALTY, "loyalty", "loyal", "return");
        this.register(Enchantment.IMPALING, "impaling", "impale", "oceandamage", "oceandmg");
        this.register(Enchantment.RIPTIDE, "riptide", "rip", "tide", "launch");
        this.register(Enchantment.CHANNELING, "channelling", "chanelling", "channeling", "chaneling", "channel");
        this.register(Enchantment.MULTISHOT, "multishot", "tripleshot");
        this.register(Enchantment.QUICK_CHARGE, "quickcharge", "quickdraw", "fastcharge", "fastdraw");
        this.register(Enchantment.PIERCING, "piercing");
        this.register(Enchantment.SOUL_SPEED, "soulspeed", "soilspeed", "sandspeed");
        this.register(Enchantment.SWIFT_SNEAK, "swiftsneak");

        this.register(Enchantment.BREACH, "breach");
        this.register(Enchantment.DENSITY, "density");
        this.register(Enchantment.WIND_BURST, "windburst", "wind", "burst");

        // 1.21.11+
        this.register(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft("lunge")), "lunge");
    }

    private void register(Enchantment enchantment, String... strings) {
        if (enchantment == null) return;
        this.essentialsEnchantments.add(new ZEssentialsEnchantment(enchantment, Arrays.asList(strings)));
    }

    @Override
    public List<String> getEnchantments() {
        return this.essentialsEnchantments.stream().map(EssentialsEnchantment::aliases).flatMap(List::stream).toList();
    }

}
