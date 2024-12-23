package fr.maxlego08.essentials.bot.config.embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public record EmbedConfiguration(EmbedTitle title, String description, EmbedColor color) {

    public EmbedBuilder toEmbed() {

        EmbedBuilder builder = new EmbedBuilder();

        if (title != null) {
            if (title.url() == null) {
                builder.setTitle(title.title());
            } else {
                builder.setTitle(title.title(), title.url());
            }
        }

        if (description != null) {
            builder.setDescription(description);
        }

        if (color != null) {
            builder.setColor(new Color(color.r(), color.g(), color.b(), color.alpha()));
        }

        return builder;
    }
}
