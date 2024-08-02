package fr.maxlego08.essentials.enchantments;

import fr.maxlego08.essentials.api.enchantment.Enchantments;
import fr.maxlego08.essentials.api.enchantment.EssentialsEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ZEnchantments implements Enchantments {

    private final List<EssentialsEnchantment> essentialsEnchantments = new ArrayList<>();

    @Override
    public Optional<EssentialsEnchantment> getEnchantments(String enchantment) {
        return essentialsEnchantments.stream().filter(essentialsEnchantment -> essentialsEnchantment.aliases().contains(enchantment.toLowerCase())).findFirst();
    }

    @Override
    public void register() {
        this.register(Enchantment.DAMAGE_ALL, "alldamage", "alldmg", "sharpness", "sharp", "dal");
        this.register(Enchantment.DAMAGE_ARTHROPODS, "ardmg", "baneofarthropods", "baneofarthropod", "arthropod", "dar");
        this.register(Enchantment.DAMAGE_UNDEAD, "undeaddamage", "smite", "du");
        this.register(Enchantment.DIG_SPEED, "digspeed", "efficiency", "minespeed", "cutspeed", "ds", "eff");
        this.register(Enchantment.DURABILITY, "durability", "dura", "unbreaking", "d");
        this.register(Enchantment.THORNS, "thorns", "highcrit", "thorn", "highercrit", "t");
        this.register(Enchantment.FIRE_ASPECT, "fireaspect", "fire", "meleefire", "meleeflame", "fa");
        this.register(Enchantment.KNOCKBACK, "knockback", "kback", "kb", "k");
        this.register(Enchantment.LOOT_BONUS_BLOCKS, "blockslootbonus", "fortune", "fort", "lbb");
        this.register(Enchantment.LOOT_BONUS_MOBS, "mobslootbonus", "mobloot", "looting", "lbm");
        this.register(Enchantment.OXYGEN, "oxygen", "respiration", "breathing", "breath", "o");
        this.register(Enchantment.PROTECTION_ENVIRONMENTAL, "protection", "prot", "protect", "p");
        this.register(Enchantment.PROTECTION_EXPLOSIONS, "explosionsprotection", "explosionprotection", "expprot", "blastprotection", "bprotection", "bprotect", "blastprotect", "pe");
        this.register(Enchantment.PROTECTION_FALL, "fallprotection", "fallprot", "featherfall", "featherfalling", "pfa");
        this.register(Enchantment.PROTECTION_FIRE, "fireprotection", "flameprotection", "fireprotect", "flameprotect", "fireprot", "flameprot", "pf");
        this.register(Enchantment.PROTECTION_PROJECTILE, "projectileprotection", "projprot", "pp");
        this.register(Enchantment.SILK_TOUCH, "silktouch", "softtouch", "st");
        this.register(Enchantment.WATER_WORKER, "waterworker", "aquaaffinity", "watermine", "ww");
        this.register(Enchantment.ARROW_FIRE, "firearrow", "flame", "flamearrow", "af");
        this.register(Enchantment.ARROW_DAMAGE, "arrowdamage", "power", "arrowpower", "ad");
        this.register(Enchantment.ARROW_KNOCKBACK, "arrowknockback", "arrowkb", "punch", "arrowpunch", "ak");
        this.register(Enchantment.ARROW_INFINITE, "infinitearrows", "infarrows", "infinity", "infinite", "unlimited", "unlimitedarrows", "ai");
        this.register(Enchantment.LUCK, "luck", "luckofsea", "luckofseas", "rodluck");
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

        // 1.21
        try {
            this.register(Enchantment.getByName("BREACH"), "breach");
            this.register(Enchantment.getByName("DENSITY"), "density");
            this.register(Enchantment.getByName("WIND_BURST"), "windburst", "wind", "burst");
        } catch (Exception ignored) {
        }
    }

    private void register(Enchantment enchantment, String... strings) {
        this.essentialsEnchantments.add(new ZEssentialsEnchantment(enchantment, Arrays.asList(strings)));
    }

    @Override
    public List<String> getEnchantments() {
        return this.essentialsEnchantments.stream().map(EssentialsEnchantment::aliases).flatMap(List::stream).toList();
    }
}
