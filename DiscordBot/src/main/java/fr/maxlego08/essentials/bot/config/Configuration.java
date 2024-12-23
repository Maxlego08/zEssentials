package fr.maxlego08.essentials.bot.config;

import fr.maxlego08.essentials.bot.utils.ConfigurationUtils;

public class Configuration extends ConfigurationUtils {

    private String botToken;
    private DiscordDatabaseConfiguration databaseConfiguration;
    private long guildId;
    private LinkConfiguration link;

    public String getBotToken() {
        return botToken;
    }

    public DiscordDatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public long getGuildId() {
        return guildId;
    }

    public LinkConfiguration getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "botToken='" + botToken + '\'' +
                ", databaseConfiguration=" + databaseConfiguration +
                ", guildId=" + guildId +
                ", link=" + link +
                '}';
    }
}
