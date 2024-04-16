package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.modules.Loadable;
import org.bukkit.Material;

/**
 * Represents a compacting configuration for materials.
 * This record encapsulates data related to compacting materials, including the source material and the resulting material.
 *
 * @param from The source material to be compacted.
 * @param to   The resulting material after compacting.
 * @see Loadable
 */
public record CompactMaterial(Material from, Material to) implements Loadable {
}
