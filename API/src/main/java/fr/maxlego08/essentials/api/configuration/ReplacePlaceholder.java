package fr.maxlego08.essentials.api.configuration;

import fr.maxlego08.essentials.api.configuration.placeholders.NumberPlaceholder;
import fr.maxlego08.essentials.api.configuration.placeholders.StringPlaceholder;

import java.util.List;

public record ReplacePlaceholder(String placeholder, String defaultValue, List<ReplacePlaceholderElement> elements) {

    public String replace(String result) {

        for (ReplacePlaceholderElement element : this.elements) {
            if (element instanceof NumberPlaceholder numberPlaceholder) {
                try {
                    long longResult = Long.parseLong(result);
                    if (longResult <= numberPlaceholder.maxValue()) {
                        return numberPlaceholder.result().replace("%result%", result);
                    }
                } catch (NumberFormatException ignored) {
                    return result + " is not a number !";
                }
            } else if (element instanceof StringPlaceholder stringPlaceholder) {
                if (result.equalsIgnoreCase(stringPlaceholder.equalsTo())) {
                    return stringPlaceholder.result().replace("%result%", result);
                }
            }
        }

        return this.defaultValue.replace("%result%", result);
    }
}
