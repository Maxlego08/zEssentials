package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.modules.Loadable;

public record ChatPlaceholder(String name, String regex, String result, String permission) implements Loadable {
}
