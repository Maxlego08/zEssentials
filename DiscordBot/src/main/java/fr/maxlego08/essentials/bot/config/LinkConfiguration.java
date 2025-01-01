package fr.maxlego08.essentials.bot.config;

import fr.maxlego08.essentials.bot.config.embed.EmbedConfiguration;

public record LinkConfiguration(boolean enable, LinkLogConfiguration log, LinkMessage messages,
                                ButtonConfiguration button, EmbedConfiguration embed) {

}
