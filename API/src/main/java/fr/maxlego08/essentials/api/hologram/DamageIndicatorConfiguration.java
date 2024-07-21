package fr.maxlego08.essentials.api.hologram;

import org.bukkit.entity.EntityType;

import java.util.List;

public record DamageIndicatorConfiguration(boolean enabled,
                                           boolean players,
                                           boolean mobs,
                                           boolean animals,
                                           boolean waterMobs,
                                           int duration,
                                           String appearance,
                                           String criticalAppearance,
                                           double height,
                                           double offsetX,
                                           double offsetY,
                                           double offsetZ,
                                           String decimalFormat,
                                           List<EntityType> disabledEntities
) {
}
