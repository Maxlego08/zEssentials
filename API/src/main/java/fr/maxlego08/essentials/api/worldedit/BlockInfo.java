package fr.maxlego08.essentials.api.worldedit;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.math.BigDecimal;

public record BlockInfo(Block block, Material newMaterial, BigDecimal price) {
}
