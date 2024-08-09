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
        this.register(valueOf("DAMAGE_ALL", "SHARPNESS"), "alldamage", "alldmg", "sharpness", "sharp", "dal");
        this.register(valueOf("DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS"), "ardmg", "baneofarthropods", "baneofarthropod", "arthropod", "dar");
        this.register(valueOf("DAMAGE_UNDEAD", "SMITE"), "undeaddamage", "smite", "du");
        this.register(valueOf("DIG_SPEED", "EFFICIENCY"), "digspeed", "efficiency", "minespeed", "cutspeed", "ds", "eff");
        this.register(valueOf("DURABILITY", "UNBREAKING"), "durability", "dura", "unbreaking", "d");
        this.register(Enchantment.THORNS, "thorns", "highcrit", "thorn", "highercrit", "t");
        this.register(Enchantment.FIRE_ASPECT, "fireaspect", "fire", "meleefire", "meleeflame", "fa");
        this.register(Enchantment.KNOCKBACK, "knockback", "kback", "kb", "k");
        this.register(valueOf("LOOT_BONUS_BLOCKS", "FORTUNE"), "blockslootbonus", "fortune", "fort", "lbb");
        this.register(valueOf("LOOT_BONUS_MOBS", "LOOTING"), "mobslootbonus", "mobloot", "looting", "lbm");
        this.register(valueOf("OXYGEN", "RESPIRATION"), "oxygen", "respiration", "breathing", "breath", "o");
        this.register(valueOf("PROTECTION_ENVIRONMENTAL", "PROTECTION"), "protection", "prot", "protect", "p");
        this.register(valueOf("PROTECTION_EXPLOSIONS", "BLAST_PROTECTION"), "explosionsprotection", "explosionprotection", "expprot", "blastprotection", "bprotection", "bprotect", "blastprotect", "pe");
        this.register(valueOf("PROTECTION_FALL", "FEATHER_FALLING"), "fallprotection", "fallprot", "featherfall", "featherfalling", "pfa");
        this.register(valueOf("PROTECTION_FIRE", "FIRE_PROTECTION"), "fireprotection", "flameprotection", "fireprotect", "flameprotect", "fireprot", "flameprot", "pf");
        this.register(valueOf("PROTECTION_PROJECTILE", "PROJECTILE_PROTECTION"), "projectileprotection", "projprot", "pp");
        this.register(Enchantment.SILK_TOUCH, "silktouch", "softtouch", "st");
        this.register(valueOf("WATER_WORKER", "AQUA_AFFINITY"), "waterworker", "aquaaffinity", "watermine", "ww");
        this.register(valueOf("ARROW_FIRE", "FLAME"), "firearrow", "flame", "flamearrow", "af");
        this.register(valueOf("ARROW_DAMAGE", "POWER"), "arrowdamage", "power", "arrowpower", "ad");
        this.register(valueOf("ARROW_KNOCKBACK", "PUNCH"), "arrowknockback", "arrowkb", "punch", "arrowpunch", "ak");
        this.register(valueOf("ARROW_INFINITE", "INFINITY"), "infinitearrows", "infarrows", "infinity", "infinite", "unlimited", "unlimitedarrows", "ai");
        this.register(valueOf("LUCK", "LUCK_OF_THE_SEA"), "luck", "luckofsea", "luckofseas", "rodluck");
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

    private Enchantment valueOf(String... names) {
        for (final String name : names) {
            try {
                Enchantment value = (Enchantment) Enchantment.class.getDeclaredField(name).get(null);
                if (value != null) {
                    return value;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return null;
    }
}
