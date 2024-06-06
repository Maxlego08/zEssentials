package fr.maxlego08.essentials.chat;

import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDisplay extends ZUtils implements ChatDisplay {

    private final String name;
    private final Pattern pattern;
    private final String result;
    private final String permission;

    public CustomDisplay(String name, String regex, String result, String permission) {
        this.name = name;
        this.pattern = Pattern.compile(regex);
        this.result = result;
        this.permission = permission;
    }

    @Override
    public String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message) {
        Matcher matcher = this.pattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();

        while (matcher.find()) matcher.appendReplacement(formattedMessage, "<" + this.name + ">");
        builder.resolver(Placeholder.component(this.name, adventureComponent.getComponent(papi(this.result.replace("%player%", sender.getName()), sender))));

        matcher.appendTail(formattedMessage);
        return formattedMessage.toString();
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(this.permission);
    }
}
