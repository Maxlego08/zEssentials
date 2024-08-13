package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.modules.Loadable;
import org.bukkit.Material;

import java.math.BigDecimal;

public record BlockPrice(Material material, BigDecimal price) implements Loadable {
}
