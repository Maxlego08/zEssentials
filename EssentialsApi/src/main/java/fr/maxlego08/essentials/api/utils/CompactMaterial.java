package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.modules.Loadable;
import org.bukkit.Material;

public record CompactMaterial(Material from, Material to) implements Loadable {
}
