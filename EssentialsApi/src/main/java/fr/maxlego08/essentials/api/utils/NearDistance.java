package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.modules.Loadable;

public record NearDistance(String permission, double distance) implements Loadable {
}
