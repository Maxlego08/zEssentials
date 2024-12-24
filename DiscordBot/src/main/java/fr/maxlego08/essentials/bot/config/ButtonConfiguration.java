package fr.maxlego08.essentials.bot.config;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public record ButtonConfiguration(String name, ButtonStyle style, boolean disabled, String emoji) {

}
