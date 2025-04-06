package fr.maxlego08.essentials.api.afk;

import fr.maxlego08.essentials.api.modules.Loadable;

public record AfkPermission(int priority, String permission, int maxAfkTime, int startAfkTime, boolean kick, String messageOnStartAfk) implements Loadable {
}
