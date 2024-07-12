package fr.maxlego08.essentials.api.discord;

import org.bukkit.entity.Player;

import java.awt.*;

public record DiscordConfiguration(boolean isEnable, String webhookUrl, AvatarType avatarType,
                                   DiscordMessageType discordMessageType, String message, String webhookColor,
                                   String username) {

    private static final String URL_ISOMETRIC = "https://mc-heads.net/head/%s";
    private static final String URL_AVATAR = "https://mc-heads.net/avatar/%s";

    public static DiscordConfiguration disabled() {
        return new DiscordConfiguration(false, null, AvatarType.NONE, DiscordMessageType.NORMAL, null, null, null);
    }

    public void apply(DiscordWebhook discordWebhook, Player player, String message) {

        if (this.username != null) {
            discordWebhook.setUsername(this.replace(this.username, player, message));
        }

        switch (this.avatarType) {
            case NONE -> {
            }
            case AVATAR -> discordWebhook.setAvatarUrl(String.format(URL_AVATAR, player.getUniqueId()));
            case ISOMETRIC -> discordWebhook.setAvatarUrl(String.format(URL_ISOMETRIC, player.getUniqueId()));
        }

        if (this.discordMessageType == DiscordMessageType.WEBHOOK) {

            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
            embedObject.setDescription(this.replace(this.message, player, message));
            embedObject.setColor(hexToColor(this.webhookColor));

            discordWebhook.addEmbed(embedObject);

        } else {

            discordWebhook.setContent(this.replace(this.message, player, message));
        }

    }

    private String replace(String value, Player player, String message) {
        return value == null ? "" : value.replace("%player%", player.getName()).replace("%message%", message == null ? "" : message);
    }

    private Color hexToColor(String hex) {
        // Retire le caractère '#' s'il est présent
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Vérifie que la chaîne de caractères contient bien 6 ou 8 caractères hexadécimaux
        if (hex.length() != 6 && hex.length() != 8) {
            throw new IllegalArgumentException("Invalid hex color string");
        }

        // Parse les composants RGB ou RGBA
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        int a = 255; // Opacité par défaut

        if (hex.length() == 8) {
            a = Integer.parseInt(hex.substring(6, 8), 16);
        }

        return new Color(r, g, b, a);
    }
}
