package fr.maxlego08.essentials.api.discord;

import fr.maxlego08.essentials.api.functionnals.ReturnConsumer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public record DiscordConfiguration(boolean isEnable, String webhookUrl, String avatarUrl, String content,
                                   String username, List<DiscordEmbedConfiguration> embeds) {

    public static DiscordConfiguration disabled() {
        return new DiscordConfiguration(false, null, null, null, null, new ArrayList<>());
    }

    public void apply(ReturnConsumer<String, String> consumer, DiscordWebhook discordWebhook, String... args) {

        if (this.username != null) {
            discordWebhook.setUsername(this.replace(consumer.accept(this.username), args));
        }

        if (this.avatarUrl != null) {
            discordWebhook.setAvatarUrl(this.replace(consumer.accept(this.avatarUrl), args));
        }

        if (this.content != null) {
            discordWebhook.setContent(this.replace(consumer.accept(this.content), args));
        }

        this.embeds.forEach(embed -> embed.apply(consumer, discordWebhook, args));
    }

    private String replace(String message, Object[] newArgs) {
        if (newArgs.length % 2 != 0) {
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
        }

        for (int i = 0; i < newArgs.length; i += 2) {
            if (newArgs[i] == null || newArgs[i + 1] == null) {
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            }
            message = message.replace(newArgs[i].toString(), newArgs[i + 1].toString());
        }
        return message;
    }
}
