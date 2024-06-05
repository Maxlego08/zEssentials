package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.modules.Loadable;

public record ChatFormat(int priority, String permission, String format) implements Loadable {
}
