package fr.maxlego08.essentials.api.discord;

public interface DiscordManager {

    DiscordConfiguration getChatConfiguration();

    DiscordConfiguration getFirstJoinConfiguration();

    DiscordConfiguration getJoinConfiguration();

    DiscordConfiguration getLeftConfiguration();

}
