package fr.maxlego08.essentials.api.configuration.placeholders;

import fr.maxlego08.essentials.api.configuration.ReplacePlaceholderElement;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholderType;

public record StringPlaceholder(String result, String equalsTo) implements ReplacePlaceholderElement {

    @Override
    public ReplacePlaceholderType getType() {
        return ReplacePlaceholderType.STRING;
    }
}
