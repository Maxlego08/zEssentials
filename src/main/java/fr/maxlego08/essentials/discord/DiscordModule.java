package fr.maxlego08.essentials.discord;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.discord.DiscordConfiguration;
import fr.maxlego08.essentials.api.discord.DiscordEmbedConfiguration;
import fr.maxlego08.essentials.api.discord.DiscordManager;
import fr.maxlego08.essentials.api.discord.DiscordWebhook;
import fr.maxlego08.essentials.api.event.events.user.UserFirstJoinEvent;
import fr.maxlego08.essentials.module.ZModule;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DiscordModule extends ZModule implements DiscordManager {

    private DiscordConfiguration chatConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration joinConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration leftConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration firstJoinConfiguration = DiscordConfiguration.disabled();

    public DiscordModule(ZEssentialsPlugin plugin) {
        super(plugin, "discord");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();


        YamlConfiguration configuration = getConfiguration();

        ConfigurationSection configurationSection = configuration.getConfigurationSection("chat-message");
        if (configurationSection != null) {
            loadConfiguration(configurationSection, d -> this.chatConfiguration = d);
        }

        configurationSection = configuration.getConfigurationSection("join-message");
        if (configurationSection != null) {
            loadConfiguration(configurationSection, d -> this.joinConfiguration = d);
        }

        configurationSection = configuration.getConfigurationSection("first-join-message");
        if (configurationSection != null) {
            loadConfiguration(configurationSection, d -> this.firstJoinConfiguration = d);
        }

        configurationSection = configuration.getConfigurationSection("left-message");
        if (configurationSection != null) {
            loadConfiguration(configurationSection, d -> this.leftConfiguration = d);
        }
    }

    private void loadConfiguration(ConfigurationSection configurationSection, Consumer<DiscordConfiguration> consumer) {

        boolean isEnable = configurationSection.getBoolean("enable");
        String webhookUrl = configurationSection.getString("webhook");
        String avatarUrl = configurationSection.getString("avatar");
        String message = configurationSection.getString("message");
        String username = configurationSection.getString("username");
        List<Map<?, ?>> values = configurationSection.getMapList("embeds");

        this.plugin.getScheduler().runAsync(wrappedTask -> {
            if (checkWebhookExists(webhookUrl)) {
                var config = new DiscordConfiguration(isEnable, webhookUrl, avatarUrl, message, username, DiscordEmbedConfiguration.convertToEmbedObjects(values));
                consumer.accept(config);
            } else {
                var config = DiscordConfiguration.disabled();
                plugin.getLogger().severe("URL " + webhookUrl + " is invalid ! Disable your discord configuration.");
                consumer.accept(config);
            }
        });
    }

    private boolean checkWebhookExists(String webhookUrl) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            return responseCode == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DiscordConfiguration getChatConfiguration() {
        return chatConfiguration;
    }

    @Override
    public DiscordConfiguration getJoinConfiguration() {
        return joinConfiguration;
    }

    @Override
    public DiscordConfiguration getLeftConfiguration() {
        return leftConfiguration;
    }

    @Override
    public DiscordConfiguration getFirstJoinConfiguration() {
        return firstJoinConfiguration;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTalk(AsyncChatEvent event) {

        if (!this.chatConfiguration.isEnable()) return;

        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        var player = event.getPlayer();
        sendDiscordMessage(player.getName(), player.getUniqueId(), message, this.chatConfiguration);
    }

    private void sendDiscordMessage(String playerName, UUID uuid, String message, DiscordConfiguration configuration) {

        DiscordWebhook discordWebhook = new DiscordWebhook(configuration.webhookUrl());
        configuration.apply(discordWebhook, playerName, uuid, message);

        plugin.getScheduler().runAsync(wrappedTask -> {
            try {
                discordWebhook.execute();
            } catch (Exception exception) {
                if (plugin.getConfiguration().isEnableDebug()) {
                    exception.printStackTrace();
                }
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (!this.joinConfiguration.isEnable()) return;

        var player = event.getPlayer();
        sendDiscordMessage(player.getName(), player.getUniqueId(), "", this.joinConfiguration);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        if (!this.leftConfiguration.isEnable()) return;

        var player = event.getPlayer();
        sendDiscordMessage(player.getName(), player.getUniqueId(), "", this.leftConfiguration);
    }

    @EventHandler
    public void onFirstJoin(UserFirstJoinEvent event) {

        if (!this.firstJoinConfiguration.isEnable()) return;

        var player = event.getUser();
        sendDiscordMessage(player.getName(), player.getUniqueId(), "", this.firstJoinConfiguration);
    }
}
