package fr.maxlego08.essentials.bot.link;

import fr.maxlego08.essentials.api.discord.DiscordAction;
import fr.maxlego08.essentials.api.dto.DiscordCodeDTO;
import fr.maxlego08.essentials.bot.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinkManager extends ListenerAdapter {

    public static final String BUTTON_LINK_NAME = "zessentials:link";
    private final DiscordBot instance;
    private final List<DiscordCodeDTO> codes = new ArrayList<>();

    public LinkManager(DiscordBot instance) {
        this.instance = instance;
    }

    public void loadCodes() {
        var codes = instance.getStorageManager().loadCodes();
        this.codes.clear();
        this.codes.addAll(codes);
    }

    public void sendLinkMessage(MessageChannelUnion textChannel) {

        EmbedBuilder builder = instance.getConfiguration().getLink().embed().toEmbed();

        var config = instance.getConfiguration().getLink().button();
        Button action = new ButtonImpl(BUTTON_LINK_NAME, config.name(), config.style(), config.disabled(), config.emoji() == null ? null : Emoji.fromUnicode(config.emoji()));

        textChannel.sendMessageEmbeds(builder.build()).addActionRow(action).queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals(BUTTON_LINK_NAME)) {
            createCode(event, event.getGuild(), event.getUser());
        }
    }

    private void createCode(ButtonInteractionEvent event, Guild guild, User user) {

        var config = instance.getConfiguration().getLink();
        var storage = instance.getStorageManager();

        storage.isAccountLinked(user.getIdLong(), isLinked -> {
            if (isLinked) {

                event.reply(config.messages().already()).setEphemeral(true).queue();
            } else {

                var optional = getCode(user.getIdLong());

                // If code already exists
                if (optional.isPresent()) {
                    var code = optional.get();
                    replyCode(code.code(), event);

                    storage.insertLog(DiscordAction.ASK_CODE, null, null, user.getEffectiveName(), user.getIdLong(), code.code());

                    this.log(guild, config.log().channel(), config.log().ask()
                            .replace("%name%", user.getName())
                            .replace("%code%", code.code())
                            .replace("%id%", String.valueOf(user.getIdLong()))
                    );

                    return;
                }

                // Otherwise, we will create one
                String generatedCode = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16);
                DiscordCodeDTO newCode = new DiscordCodeDTO(generatedCode, user.getIdLong(), user.getName());
                this.codes.add(newCode);
                replyCode(generatedCode, event);
                storage.saveCode(newCode);
                storage.insertLog(DiscordAction.CREATE_CODE, null, null, user.getEffectiveName(), user.getIdLong(), generatedCode);

                this.log(guild, config.log().channel(), config.log().create()
                        .replace("%name%", user.getName())
                        .replace("%code%", generatedCode)
                        .replace("%id%", String.valueOf(user.getIdLong()))
                );
            }
        });
    }

    private void log(Guild guild, long channelId, String message) {

        var channel = guild.getTextChannelById(channelId);
        if (channel == null) {
            System.err.println("Channel " + channelId + " not found");
            return;
        }

        channel.sendMessage(message).queue();
    }

    private void replyCode(String code, ButtonInteractionEvent event) {
        var config = instance.getConfiguration().getLink();
        event.reply(config.messages().code().replace("%code%", code)).setEphemeral(true).queue();
    }

    private Optional<DiscordCodeDTO> getCode(long userId) {
        return this.codes.stream().filter(code -> code.user_id() == userId).findFirst();
    }
}
