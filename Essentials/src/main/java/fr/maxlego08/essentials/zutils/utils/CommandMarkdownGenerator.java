package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.commands.EssentialsCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandMarkdownGenerator {

    public void generateMarkdownFile(List<EssentialsCommand> commands, Path filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Markdown table header
        sb.append("| Command | Aliases | Permission | Description |\n");
        sb.append("|---------|---------|------------|-------------|\n");

        List<EssentialsCommand> essentialsCommands = new ArrayList<>();

        commands.stream().filter(e -> e.getParent() == null).sorted(Comparator.comparing(EssentialsCommand::getMainCommand)).forEach(command -> {
            essentialsCommands.add(command);
            essentialsCommands.addAll(commands.stream().filter(e -> e.getMainParent() == command).sorted(Comparator.comparing(EssentialsCommand::getMainCommand)).toList());
        });

        for (EssentialsCommand command : essentialsCommands) {
            // Gather command data
            String cmd = command.getSyntax(); // Assuming getSyntax() gives the command
            List<String> aliasesList = new ArrayList<>(command.getSubCommands());
            if (!aliasesList.isEmpty()) {
                aliasesList.remove(0);  // Remove the first element
            }
            String aliases = aliasesList.stream()
                    .map(alias -> "/" + alias)  // Add '/' before each alias
                    .collect(Collectors.joining(", "));
            String perm = command.getPermission(); // getPermission() for permissions
            String desc = command.getDescription(); // getDescription() for the description

            // Escape special Markdown characters in descriptions
            desc = desc == null ? "" : desc.replace("|", "\\|");
            perm = perm == null ? "" : perm;

            // Add row to the Markdown table
            sb.append(String.format("| `%s` | %s | %s | %s |\n", cmd, aliases, perm, desc));
        }

        // Write the StringBuilder content to the file
        Files.writeString(filePath, sb.toString());
    }
}
