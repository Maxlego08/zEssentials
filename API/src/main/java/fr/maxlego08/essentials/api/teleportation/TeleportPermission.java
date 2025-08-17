package fr.maxlego08.essentials.api.teleportation;

import fr.maxlego08.essentials.api.modules.Loadable;

public record TeleportPermission(String permission, int delay) implements Loadable {

}