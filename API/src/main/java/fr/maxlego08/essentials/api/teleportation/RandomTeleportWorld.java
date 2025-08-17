package fr.maxlego08.essentials.api.teleportation;

import fr.maxlego08.essentials.api.modules.Loadable;

public record RandomTeleportWorld(String world, int centerX, int centerZ, int radiusX, int radiusZ) implements Loadable {
}
