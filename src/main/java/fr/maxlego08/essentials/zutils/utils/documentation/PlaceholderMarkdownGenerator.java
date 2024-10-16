package fr.maxlego08.essentials.zutils.utils.documentation;

import fr.maxlego08.essentials.placeholders.AutoPlaceholder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderMarkdownGenerator {

    public void generateMarkdownFile(List<AutoPlaceholder> placeholders, Path filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Markdown table header
        sb.append("| Placeholder | Description |\n");
        sb.append("|--------------|-------------|\n");

        placeholders.sort(Comparator.comparing(AutoPlaceholder::getStartWith));
        for (AutoPlaceholder placeholder : placeholders) {
            // Format placeholder with arguments
            String placeholderText = "%zessentials_" + placeholder.getStartWith();
            if (!placeholder.getArgs().isEmpty()) {
                String args = placeholder.getArgs().stream().map(argument -> "<" + argument + ">").collect(Collectors.joining("_"));
                placeholderText += args;
            }
            placeholderText += "%";

            // Escape Markdown special characters in descriptions
            String desc = placeholder.getDescription().replace("|", "\\|");

            // Add row to the Markdown table
            sb.append(String.format("| `%s` | %s |\n", placeholderText, desc));
        }

        // Write the StringBuilder content to the file
        Files.writeString(filePath, sb.toString());
    }
}
