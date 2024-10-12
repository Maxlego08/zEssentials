package fr.maxlego08.essentials.zutils.utils.documentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class PermissionMarkdownGenerator {

    public void generateMarkdownFile(List<PermissionInfo> permissions, Path filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Markdown table header
        sb.append("| Permission | Description |\n");
        sb.append("|--------------|-------------|\n");

        permissions.sort(Comparator.comparing(PermissionInfo::permission));
        for (PermissionInfo permissionInfo : permissions) {
            // Format placeholder with arguments
            String placeholderText = permissionInfo.permission();

            // Escape Markdown special characters in descriptions
            String desc = permissionInfo.description();

            // Add row to the Markdown table
            sb.append(String.format("| `%s` | %s |\n", placeholderText, desc));
        }

        // Write the StringBuilder content to the file
        Files.writeString(filePath, sb.toString());
    }
}
