package fr.maxlego08.essentials.chat;

import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerPingDisplay implements ChatDisplay {

    private final Pattern pingPattern = Pattern.compile("@[a-zA-Z0-9_]{3,16}");
    private final String playerPingColor;
    private final String playerPingColorOther;
    private final Sound playerPingSound;
    private final float playerPingSoundVolume;
    private final float playerPingSoundPitch;

    public PlayerPingDisplay(String playerPingColor, String playerPingColorOther, Sound playerPingSound, float playerPingSoundVolume, float playerPingSoundPitch) {
        this.playerPingColor = playerPingColor;
        this.playerPingColorOther = playerPingColorOther;
        this.playerPingSound = playerPingSound;
        this.playerPingSoundVolume = playerPingSoundVolume;
        this.playerPingSoundPitch = playerPingSoundPitch;
    }

    @Override
    public String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message) {

        Matcher matcher = this.pingPattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();
        boolean shouldSendSound = false;

        while (matcher.find()) {
            String match = matcher.group();
            String playerName = match.substring(1);  // Assuming match starts with a special character (e.g., '@')
            boolean isReceiver = playerName.equalsIgnoreCase(receiver.getName());
            if (isReceiver) {
                shouldSendSound = true;
            }

            String placeholderTag = "ping_" + playerName.toLowerCase();
            String resolvedName = isReceiver ? this.playerPingColor : this.playerPingColorOther;
            resolvedName = resolvedName.replace("%name%", match);

            builder.resolver(Placeholder.component(placeholderTag, adventureComponent.getComponent(resolvedName)));
            matcher.appendReplacement(formattedMessage, "<" + placeholderTag + ">");
        }
        matcher.appendTail(formattedMessage);
        message = formattedMessage.toString();

        if (shouldSendSound) {
            receiver.playSound(receiver.getLocation(), this.playerPingSound, this.playerPingSoundVolume, this.playerPingSoundPitch);
        }

        return message;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return true;
    }
}
