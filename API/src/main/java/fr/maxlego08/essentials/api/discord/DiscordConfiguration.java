package fr.maxlego08.essentials.api.discord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record DiscordConfiguration(boolean isEnable, String webhookUrl, String avatarUrl, String content,
                                   String username, List<DiscordEmbedConfiguration> embeds) {

    public static DiscordConfiguration disabled() {
        return new DiscordConfiguration(false, null, null, null, null, new ArrayList<>());
    }

    public void apply(DiscordWebhook discordWebhook, String playerName, UUID playerUuid, String message) {

        if (this.username != null) {
            discordWebhook.setUsername(this.replace(this.username, playerName, message, playerUuid));
        }

        if (this.avatarUrl != null) {
            discordWebhook.setAvatarUrl(this.replace(this.avatarUrl, playerName, message, playerUuid));
        }

        if (this.content != null) {
            discordWebhook.setContent(this.replace(this.content, playerName, message, playerUuid));
        }

        this.embeds.forEach(embed -> embed.apply(discordWebhook, playerName, playerUuid, message));
    }

    private String replace(String value, String playerName, String message, UUID uuid) {
        return value == null ? "" : value.replace("%player%", playerName).replace("%message%", message == null ? "" : message).replace("%uuid%", uuid.toString());
    }
}
