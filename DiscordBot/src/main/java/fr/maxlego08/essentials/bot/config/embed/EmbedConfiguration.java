package fr.maxlego08.essentials.bot.config.embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public record EmbedConfiguration(TitleConfiguration title, String description, ColorConfiguration color,
                                 FooterConfiguration footer, ImageConfiguration image, ThumbnailConfiguration thumbnail,
                                 AuthorConfiguration author, List<FieldConfiguration> fields) {

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


        if (footer != null) {
            builder.setFooter(footer.text(), footer.iconUrl());
        }

        if (image != null) {
            builder.setImage(image.url());
        }

        if (thumbnail != null) {
            builder.setThumbnail(thumbnail.url());
        }

        if (author != null) {
            builder.setAuthor(author.name(), author.url(), author.iconUrl());
        }

        if (fields != null) {
            fields.forEach(field -> builder.addField(field.name(), field.value(), field.inline()));
        }

        return builder;
    }
}
