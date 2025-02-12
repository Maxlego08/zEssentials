package fr.maxlego08.essentials.discord;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.discord.DiscordAccount;
import fr.maxlego08.essentials.api.discord.DiscordAction;
import fr.maxlego08.essentials.api.discord.DiscordConfiguration;
import fr.maxlego08.essentials.api.discord.DiscordEmbedConfiguration;
import fr.maxlego08.essentials.api.discord.DiscordManager;
import fr.maxlego08.essentials.api.discord.DiscordWebhook;
import fr.maxlego08.essentials.api.dto.DiscordAccountDTO;
import fr.maxlego08.essentials.api.event.events.discord.DiscordLinkEvent;
import fr.maxlego08.essentials.api.event.events.discord.DiscordUnlinkEvent;
import fr.maxlego08.essentials.api.event.events.user.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DiscordModule extends ZModule implements DiscordManager {

    private final Map<String, Boolean> webhookUrlCache = new HashMap<>();
    private DiscordConfiguration logLinkErrorConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration logLinkSuccessConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration logUnlinkConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration chatConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration joinConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration leftConfiguration = DiscordConfiguration.disabled();
    private DiscordConfiguration firstJoinConfiguration = DiscordConfiguration.disabled();
    private boolean enableLinkAccount;

    public DiscordModule(ZEssentialsPlugin plugin) {
        super(plugin, "discord");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        YamlConfiguration configuration = getConfiguration();

        loadConfiguration(configuration.getConfigurationSection("chat-message"), d -> this.chatConfiguration = d);
        loadConfiguration(configuration.getConfigurationSection("join-message"), d -> this.joinConfiguration = d);
        loadConfiguration(configuration.getConfigurationSection("first-join-message"), d -> this.firstJoinConfiguration = d);
        loadConfiguration(configuration.getConfigurationSection("left-message"), d -> this.leftConfiguration = d);
        loadConfiguration(configuration.getConfigurationSection("log-link-error-message"), d -> this.logLinkErrorConfiguration = d);
        loadConfiguration(configuration.getConfigurationSection("log-link-success-message"), d -> this.logLinkSuccessConfiguration = d);
        loadConfiguration(configuration.getConfigurationSection("log-unlink-message"), d -> this.logUnlinkConfiguration = d);
    }

    private void loadConfiguration(ConfigurationSection configurationSection, Consumer<DiscordConfiguration> consumer) {

        if (configurationSection == null) return;

        boolean isEnable = configurationSection.getBoolean("enable");
        String webhookUrl = configurationSection.getString("webhook");
        String avatarUrl = configurationSection.getString("avatar");
        String message = configurationSection.getString("message");
        String username = configurationSection.getString("username");
        List<Map<?, ?>> values = configurationSection.getMapList("embeds");

        if (checkWebhookExists(webhookUrl)) {
            var config = new DiscordConfiguration(isEnable, webhookUrl, avatarUrl, message, username, DiscordEmbedConfiguration.convertToEmbedObjects(values));
            consumer.accept(config);
        } else {
            var config = DiscordConfiguration.disabled();
            if (isEnable) {
                plugin.getLogger().severe("URL " + webhookUrl + " is invalid ! Disable your discord configuration.");
            }
            consumer.accept(config);
        }
    }

    private boolean checkWebhookExists(String webhookUrl) {
        if (this.webhookUrlCache.containsKey(webhookUrl)) {
            return this.webhookUrlCache.get(webhookUrl);
        }

        try {
            URL url = new URI(webhookUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            this.webhookUrlCache.put(webhookUrl, responseCode == 200);
            return responseCode == 200;
        } catch (Exception exception) {
            exception.printStackTrace();
            this.webhookUrlCache.put(webhookUrl, false);
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
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        var player = event.getPlayer();
        sendDiscordMessage(player, player.getName(), player.getUniqueId(), message, this.chatConfiguration);
    }

    private void sendDiscordMessage(Player player, String playerName, UUID uuid, String message, DiscordConfiguration configuration) {
        sendDiscord(player, configuration, "%player%", playerName, "%uuid%", uuid.toString(), "%message%", message);
    }

    private void sendDiscord(Player player, DiscordConfiguration configuration, String... args) {

        if (!configuration.isEnable()) return;

        DiscordWebhook discordWebhook = new DiscordWebhook(configuration.webhookUrl());
        configuration.apply(text -> text == null ? null : player == null ? text : papi(text, player), discordWebhook, args);

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
        var player = event.getPlayer();
        sendDiscordMessage(player, player.getName(), player.getUniqueId(), "", this.joinConfiguration);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        sendDiscordMessage(player, player.getName(), player.getUniqueId(), "", this.leftConfiguration);
    }

    @EventHandler
    public void onFirstJoin(UserFirstJoinEvent event) {
        var user = event.getUser();
        sendDiscordMessage(user.getPlayer(), user.getName(), user.getUniqueId(), "", this.firstJoinConfiguration);
    }

    @Override
    public void linkAccount(User user, String discordCode) {

        if (!enableLinkAccount) {
            message(user, Message.COMMAND_LINK_ACCOUNT_DISABLED);
            return;
        }

        if (user.isDiscordLinked()) {
            message(user, Message.COMMAND_LINK_ACCOUNT_ALREADY_LINKED);
            return;
        }

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        this.plugin.getScheduler().runAsync(wrappedTask -> {

            var optional = iStorage.selectCode(discordCode);
            if (optional.isEmpty()) {
                message(user, Message.COMMAND_LINK_ACCOUNT_INVALID_CODE);
                iStorage.insertDiscordLog(DiscordAction.TRY_LINK_ACCOUNT, user.getUniqueId(), user.getName(), null, -1, discordCode);
                sendDiscord(user.getPlayer(), this.logLinkErrorConfiguration, "%player%", user.getName(), "%code%", discordCode);
                return;
            }

            var code = optional.get();
            DiscordAccount discordAccount = new DiscordAccountDTO(code.user_id(), user.getUniqueId(), user.getName(), code.discord_name(), new Timestamp(System.currentTimeMillis()));

            this.plugin.getScheduler().runNextTick(w2 -> {

                var event = new DiscordLinkEvent(user, discordAccount);
                event.callEvent();
                if (event.isCancelled()) return;

                iStorage.clearCode(code);
                iStorage.linkDiscordAccount(user.getUniqueId(), user.getName(), code.discord_name(), code.user_id());
                iStorage.insertDiscordLog(DiscordAction.LINK_ACCOUNT, user.getUniqueId(), user.getName(), code.discord_name(), code.user_id(), code.code());

                user.setDiscordAccount(discordAccount);

                message(user, Message.COMMAND_LINK_ACCOUNT_SUCCESS, "%discord%", code.discord_name());

                sendDiscord(user.getPlayer(), this.logLinkSuccessConfiguration, "%player%", user.getName(), "%discord-name%", code.discord_name(), "%discord-id%", String.valueOf(code.user_id()), "%code%", code.code());
            });
        });
    }

    public void unlinkAccount(User user) {

        if (!enableLinkAccount) {
            message(user, Message.COMMAND_LINK_ACCOUNT_DISABLED);
        }

        if (!user.isDiscordLinked()) {
            message(user, Message.COMMAND_LINK_ACCOUNT_NOT_LINKED);
            return;
        }

        var discordAccount = user.getDiscordAccount();

        var event = new DiscordUnlinkEvent(user, discordAccount);
        event.callEvent();
        if (event.isCancelled()) return;

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        iStorage.unlinkDiscordAccount(user.getUniqueId());
        iStorage.insertDiscordLog(DiscordAction.UNLINK_ACCOUNT, user.getUniqueId(), user.getName(), discordAccount.getDiscordName(), discordAccount.getUserId(), null);

        user.removeDiscordAccount();

        message(user, Message.COMMAND_LINK_ACCOUNT_RESET);

        sendDiscord(user.getPlayer(), this.logUnlinkConfiguration, "%player%", user.getName(), "%discord-name%", discordAccount.getDiscordName(), "%discord-id%", String.valueOf(discordAccount.getUserId()));
    }
}
