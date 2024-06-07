package fr.maxlego08.essentials.chat;

import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandDisplay implements ChatDisplay {

    private final Pattern pattern = Pattern.compile("\\./\\S*");
    private final String result;
    private final String permission;

    public CommandDisplay(String result, String permission) {
        this.result = result;
        this.permission = permission;
    }

    @Override
    public String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message) {

        Matcher matcher = this.pattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();

        while (matcher.find()) {

            String match = matcher.group();
            String command = match.substring(2);

            String placeholderTag = "cmd_" + command;

            builder.resolver(Placeholder.component(placeholderTag, adventureComponent.getComponent(result.replace("%command%", command))));
            matcher.appendReplacement(formattedMessage, "<" + placeholderTag + ">");
        }

        matcher.appendTail(formattedMessage);
        return formattedMessage.toString();
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(this.permission);
    }
}
