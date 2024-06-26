package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.util.regex.Pattern;

public record CustomRules(Pattern pattern, String permission, String message) implements Loadable {
}
