package fr.maxlego08.essentials.discord;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.discord.AvatarType;
import fr.maxlego08.essentials.api.discord.DiscordConfiguration;
import fr.maxlego08.essentials.api.discord.DiscordManager;
import fr.maxlego08.essentials.api.discord.DiscordMessageType;
import fr.maxlego08.essentials.api.discord.DiscordWebhook;
import fr.maxlego08.essentials.module.ZModule;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class DiscordModule extends ZModule implements DiscordManager {

    private DiscordConfiguration chatConfiguration = DiscordConfiguration.disabled();

    public DiscordModule(ZEssentialsPlugin plugin) {
        super(plugin, "discord");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();


        YamlConfiguration configuration = getConfiguration();

        ConfigurationSection configurationSection = configuration.getConfigurationSection("chat-message");
        if (configurationSection != null) {
            this.chatConfiguration = loadConfiguration(configurationSection);
        }
    }

    private DiscordConfiguration loadConfiguration(ConfigurationSection configurationSection) {

        boolean isEnable = configurationSection.getBoolean("enable");
        String webhookUrl = configurationSection.getString("webhook");
        AvatarType avatarType = AvatarType.valueOf(configurationSection.getString("avatar-type", "NONE").toUpperCase());
        DiscordMessageType discordMessageType = DiscordMessageType.valueOf(configurationSection.getString("type", "NORMAL").toUpperCase());
        String message = configurationSection.getString("message", "Empty message !");
        String webhookColor = configurationSection.getString("webhook-color");
        String username = configurationSection.getString("username");

        return new DiscordConfiguration(isEnable, webhookUrl, avatarType, discordMessageType, message, webhookColor, username);
    }

    @Override
    public DiscordConfiguration getChatConfiguration() {
        return chatConfiguration;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTalk(AsyncChatEvent event) {

        if (!this.chatConfiguration.isEnable()) return;

        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        sendDiscordMessage(event.getPlayer(), message, this.chatConfiguration);
    }

    private void sendDiscordMessage(Player player, String message, DiscordConfiguration configuration) {

        DiscordWebhook discordWebhook = new DiscordWebhook(configuration.webhookUrl());
        configuration.apply(discordWebhook, player, message);

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
}
