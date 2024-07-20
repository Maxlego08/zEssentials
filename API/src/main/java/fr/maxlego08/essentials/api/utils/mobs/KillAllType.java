package fr.maxlego08.essentials.api.utils.mobs;

import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.WaterMob;

/**
 * @author EssentialsX
 * <a href="https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/com/earth2me/essentials/commands/Commandremove.java">https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/com/earth2me/essentials/commands/Commandremove.java</a>
 */
public enum KillAllType {
    ALL, AMBIENT, ANIMALS, ARROWS, BOATS, CUSTOM, DROPS, ENDERCRYSTALS, ENTITIES, HOSTILE, ITEMFRAMES, MINECARTS, MOBS, MONSTERS, NAMED, PAINTINGS, PASSIVE, TAMED, XP;

    public boolean checkType(Entity entity) {
        return switch (this) {
            case DROPS -> entity instanceof Item;
            case ARROWS -> entity instanceof Projectile;
            case BOATS -> entity instanceof Boat;
            case MINECARTS -> entity instanceof Minecart;
            case XP -> entity instanceof ExperienceOrb;
            case PAINTINGS -> entity instanceof Painting;
            case ITEMFRAMES -> entity instanceof ItemFrame;
            case ENDERCRYSTALS -> entity instanceof EnderCrystal;
            case AMBIENT -> entity instanceof Flying;
            case HOSTILE, MONSTERS -> entity instanceof Monster || entity instanceof ComplexLivingEntity || entity instanceof Flying || entity instanceof Slime;
            case PASSIVE, ANIMALS -> entity instanceof Animals || entity instanceof NPC || entity instanceof Snowman || entity instanceof WaterMob || entity instanceof Ambient;
            case MOBS -> entity instanceof Animals || entity instanceof NPC || entity instanceof Snowman || entity instanceof WaterMob || entity instanceof Monster || entity instanceof ComplexLivingEntity || entity instanceof Flying || entity instanceof Slime || entity instanceof Ambient;
            case ENTITIES, ALL -> true;
            default -> false;
        };
    }
}
