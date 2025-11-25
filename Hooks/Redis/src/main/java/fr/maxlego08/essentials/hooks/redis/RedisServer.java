package fr.maxlego08.essentials.hooks.redis;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.cache.ExpiringCache;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.server.messages.ChatClear;
import fr.maxlego08.essentials.api.server.messages.ChatToggle;
import fr.maxlego08.essentials.api.server.messages.ClearCooldown;
import fr.maxlego08.essentials.api.server.messages.CrossServerTeleport;
import fr.maxlego08.essentials.api.server.messages.KickMessage;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.server.messages.ServerPrivateMessage;
import fr.maxlego08.essentials.api.server.messages.UpdateCooldown;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.teleport.CrossServerLocation;
import fr.maxlego08.essentials.api.teleport.TeleportType;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.api.utils.PlayerCache;
import fr.maxlego08.essentials.hooks.redis.listener.ChatClearListener;
import fr.maxlego08.essentials.hooks.redis.listener.ChatToggleListener;
import fr.maxlego08.essentials.hooks.redis.listener.ClearCooldownListener;
import fr.maxlego08.essentials.hooks.redis.listener.CrossServerTeleportListener;
import fr.maxlego08.essentials.hooks.redis.listener.KickListener;
import fr.maxlego08.essentials.hooks.redis.listener.MessageListener;
import fr.maxlego08.essentials.hooks.redis.listener.PrivateMessageListener;
import fr.maxlego08.essentials.hooks.redis.listener.UpdateCooldownListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RedisServer implements EssentialsServer, Listener {

    private final RedisSubscriberRunnable redisSubscriberRunnable;
    private final PlayerCache playerCache = new PlayerCache();
    private final UUID instanceId = UUID.randomUUID();
    private final EssentialsPlugin plugin;
    private final EssentialsUtils utils;
    private final String playersKey = "essentials:playerlist";
    private final String playerServerKey = "essentials:playerserver:";
    private final ExpiringCache<String, Boolean> onlineCache = new ExpiringCache<>(1000 * 30);
    private final ExpiringCache<String, String> playerServerCache = new ExpiringCache<>(1000 * 10);
    private JedisPool jedisPool;

    public RedisServer(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
        this.redisSubscriberRunnable = new RedisSubscriberRunnable(plugin, this);
    }

    @Override
    public void onEnable() {

        RedisConfiguration redisConfiguration = this.plugin.getConfiguration().getRedisConfiguration();

        String password = redisConfiguration.password();
        if (password == null) {
            this.jedisPool = new JedisPool(redisConfiguration.host(), redisConfiguration.port());
        } else {
            JedisPoolConfig poolConfigBuilder = new JedisPoolConfig();
            this.jedisPool = new JedisPool(poolConfigBuilder, redisConfiguration.host(), redisConfiguration.port(), Protocol.DEFAULT_TIMEOUT, password);
        }

        this.plugin.getLogger().info("Redis connected to " + redisConfiguration.host() + ":" + redisConfiguration.port());

        // Register listeners
        this.registerListener();

        Thread thread = new Thread(this.redisSubscriberRunnable);
        thread.start();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);

        // Fetch players name every minute
        plugin.getScheduler().runTimerAsync(() -> this.playerCache.setPlayers(jedisPool.getResource().smembers(this.playersKey)), 1, 1, TimeUnit.MINUTES);
    }

    private void registerListener() {
        this.redisSubscriberRunnable.registerListener(KickMessage.class, new KickListener(this.utils));
        this.redisSubscriberRunnable.registerListener(ServerMessage.class, new MessageListener(this.utils));
        this.redisSubscriberRunnable.registerListener(ChatClear.class, new ChatClearListener(this));
        this.redisSubscriberRunnable.registerListener(ChatToggle.class, new ChatToggleListener(this.utils));
        this.redisSubscriberRunnable.registerListener(ServerPrivateMessage.class, new PrivateMessageListener(this.plugin));
        this.redisSubscriberRunnable.registerListener(ClearCooldown.class, new ClearCooldownListener(this.utils));
        this.redisSubscriberRunnable.registerListener(UpdateCooldown.class, new UpdateCooldownListener(this.utils));
        this.redisSubscriberRunnable.registerListener(CrossServerTeleport.class, new CrossServerTeleportListener(this.plugin));
    }

    @Override
    public void onDisable() {
        if (this.jedisPool != null) {
            execute(jedis -> jedis.srem(this.playersKey, Bukkit.getOnlinePlayers().stream().map(Player::getName).toList().toArray(new String[0])), false);
            this.jedisPool.close();
        }
    }

    @Override
    public List<String> getPlayersNames() {
        return new ArrayList<>(this.playerCache.getPlayers());
    }

    @Override
    public List<String> getOfflinePlayersNames() {
        return this.plugin.getConfiguration().isEnableOfflinePlayersName() ? Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList() : getPlayersNames();
    }

    @Override
    public void sendMessage(UUID uuid, Message message, Object... objects) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) { // If player is online, send the message
            utils.message(uuid, message, objects);
            return;
        }

        sendMessage(ServerMessage.single(uuid, message, objects));
    }

    @Override
    public void broadcastMessage(Permission permission, Message message, Object... objects) {

        this.utils.broadcast(permission, message, objects);
        sendMessage(new ServerMessage(ServerMessageType.BROADCAST_PERMISSION, null, permission, null, message, objects));
    }

    @Override
    public void broadcastMessage(Option option, Message message, Object... objects) {
        this.utils.broadcast(option, message, objects);
        sendMessage(new ServerMessage(ServerMessageType.BROADCAST_OPTION, null, null, option, message, objects));
    }

    @Override
    public void broadcast(String message) {
        this.utils.broadcast(Message.COMMAND_CHAT_BROADCAST, "%message%", message);
        sendMessage(new ServerMessage(ServerMessageType.BROADCAST, null, null, null, Message.COMMAND_CHAT_BROADCAST, new String[]{"%message%", message}));
    }

    @Override
    public void pub(Player player, String message) {
        this.utils.broadcast(Message.COMMAND_PUB, "%message%", message, "%player%", player.getName());
        sendMessage(new ServerMessage(ServerMessageType.BROADCAST, null, null, null, Message.COMMAND_PUB, new String[]{"%message%", message, "%player%", player.getName()}));
    }

    @Override
    public void sendPrivateMessage(User user, PrivateMessage privateMessage, String message) {

        // First check if player is online on this server
        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        User targetUser = iStorage.getUser(privateMessage.uuid());
        if (targetUser == null) {

            ServerPrivateMessage serverPrivateMessage = new ServerPrivateMessage(user.getUniqueId(), user.getName(), privateMessage.uuid(), message);
            sendMessage(serverPrivateMessage);
            return;
        }

        PrivateMessage privateMessageReply = targetUser.setPrivateMessage(user.getUniqueId(), user.getName());
        this.plugin.getUtils().sendPrivateMessage(targetUser, privateMessageReply, Message.COMMAND_MESSAGE_OTHER, message);
    }

    @Override
    public void kickPlayer(UUID uuid, Message message, Object... objects) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            utils.kick(player, message, objects);
            return;
        }

        sendMessage(new KickMessage(uuid, message, objects));
    }

    @Override
    public boolean isOnline(String userName) {
        return this.onlineCache.get(userName, () -> jedisPool.getResource().sismember(playersKey, userName));
    }

    @Override
    public void clearChat(CommandSender sender) {
        clearLocalChat();
        sendMessage(new ChatClear());
    }

    @Override
    public void toggleChat(boolean value) {
        this.utils.toggleChat(value);
        sendMessage(new ChatToggle(value));
    }

    @Override
    public void deleteCooldown(UUID uniqueId, String cooldownName) {
        this.utils.deleteCooldown(uniqueId, cooldownName);
        sendMessage(new ClearCooldown(uniqueId, cooldownName));
    }

    @Override
    public void updateCooldown(UUID uniqueId, String cooldownName, long expiredAt) {
        this.utils.updateCooldown(uniqueId, cooldownName, expiredAt);
        sendMessage(new UpdateCooldown(uniqueId, cooldownName, expiredAt));
    }

    public void clearLocalChat() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            for (int i = 0; i != 300; i++) player.sendMessage(Component.text(""));
            this.utils.message(player, Message.COMMAND_CHAT_CLEAR);
        });
        this.utils.message(Bukkit.getConsoleSender(), Message.COMMAND_CHAT_CLEAR);
    }

    private <T> void sendMessage(T message) {
        this.plugin.getScheduler().runAsync(wrappedTask -> {
            String jsonMessage = this.plugin.getGson().toJson(new RedisMessage<>(this.instanceId, message, message.getClass().getName()));
            this.plugin.debug("Send: " + jsonMessage);
            jedisPool.getResource().publish(RedisSubscriberRunnable.messagesChannel, jsonMessage);
        });
    }

    public EssentialsPlugin getPlugin() {
        return plugin;
    }

    public EssentialsUtils getUtils() {
        return utils;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public UUID getInstanceId() {
        return instanceId;
    }

    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String serverName = getServerName();
        
        this.playerCache.addPlayer(playerName);
        execute(jedis -> {
            jedis.sadd(this.playersKey, playerName);
            jedis.setex(this.playerServerKey + playerName.toLowerCase(), 300, serverName);
        });
        
        // Handle pending cross-server teleports
        CrossServerTeleportListener.onPlayerJoin(this.plugin, player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        this.playerCache.removePlayer(playerName);
        execute(jedis -> {
            jedis.srem(this.playersKey, playerName);
            jedis.del(this.playerServerKey + playerName.toLowerCase());
        });
    }

    private void execute(Consumer<Jedis> consumer) {
        execute(consumer, true);
    }

    private void execute(Consumer<Jedis> consumer, boolean async) {
        Runnable runnable = () -> {
            try {
                if (!jedisPool.isClosed()) {
                    consumer.accept(jedisPool.getResource());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        };
        if (async) this.plugin.getScheduler().runAsync(wrappedTask -> runnable.run());
        else runnable.run();
    }

    @Override
    public void sendToServer(Player player, String serverName) {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArray);
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(this.plugin, "BungeeCord", byteArray.toByteArray());
        } catch (IOException exception) {
            this.plugin.getLogger().severe("Failed to send player to server: " + exception.getMessage());
        }
    }

    @Override
    public void crossServerTeleport(Player player, TeleportType teleportType, CrossServerLocation destination) {
        String currentServer = getServerName();
        
        if (destination.isSameServer(currentServer)) {
            // Same server, teleport locally
            User user = this.plugin.getUser(player.getUniqueId());
            if (user != null) {
                user.teleport(destination.toSafeLocation().getLocation());
            }
            return;
        }
        
        // Different server - send teleport request via Redis then transfer player
        CrossServerTeleport teleportRequest = new CrossServerTeleport(
                player.getUniqueId(),
                player.getName(),
                teleportType,
                destination
        );
        
        sendMessage(teleportRequest);
        
        // Send player to target server
        this.utils.message(player, Message.TELEPORT_CROSS_SERVER_CONNECTING, "%server%", destination.getServerName());
        
        plugin.getScheduler().runLater(() -> sendToServer(player, destination.getServerName()), 10L);
    }

    @Override
    public void crossServerTeleportToPlayer(Player player, TeleportType teleportType, String targetPlayerName, String targetServer) {
        if (targetServer == null) {
            targetServer = findPlayerServer(targetPlayerName);
        }
        
        if (targetServer == null) {
            this.utils.message(player, Message.TELEPORT_CROSS_SERVER_PLAYER_NOT_FOUND, "%player%", targetPlayerName);
            return;
        }
        
        String currentServer = getServerName();
        if (targetServer.equalsIgnoreCase(currentServer)) {
            // Player is on same server, handle locally
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (targetPlayer != null) {
                User user = this.plugin.getUser(player.getUniqueId());
                if (user != null) {
                    user.teleport(targetPlayer.getLocation());
                }
            }
            return;
        }
        
        // Cross-server TPA - send request and transfer
        CrossServerTeleport teleportRequest = new CrossServerTeleport(
                player.getUniqueId(),
                player.getName(),
                teleportType,
                null,
                targetPlayerName
        );
        
        sendMessage(teleportRequest);
        
        this.utils.message(player, Message.TELEPORT_CROSS_SERVER_CONNECTING, "%server%", targetServer);
        
        String finalTargetServer = targetServer;
        plugin.getScheduler().runLater(() -> sendToServer(player, finalTargetServer), 10L);
    }

    @Override
    public String getServerName() {
        return this.plugin.getConfiguration().getServerName();
    }

    @Override
    public String findPlayerServer(String playerName) {
        // First check local
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return getServerName();
        }
        
        // Check Redis cache
        return this.playerServerCache.get(playerName.toLowerCase(), () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(this.playerServerKey + playerName.toLowerCase());
            } catch (Exception e) {
                return null;
            }
        });
    }
}
