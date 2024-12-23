package fr.maxlego08.essentials.bot.link;

import fr.maxlego08.essentials.bot.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LinkManager {

    private final DiscordBot instance;

    public LinkManager(DiscordBot instance) {
        this.instance = instance;
    }

    public void sendLinkMessage(SlashCommandInteractionEvent event, MessageChannelUnion textChannel) {

        EmbedBuilder builder = instance.getConfiguration().getLink().embed().toEmbed();

        textChannel.sendMessageEmbeds(builder.build()).queue();

    }
}
