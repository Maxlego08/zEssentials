package fr.maxlego08.essentials.server.redis;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.server.messages.ChatClear;
import fr.maxlego08.essentials.api.server.messages.ChatToggle;
import fr.maxlego08.essentials.api.server.messages.KickMessage;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.api.utils.PlayerCache;
import fr.maxlego08.essentials.server.redis.listener.ChatClearListener;
import fr.maxlego08.essentials.server.redis.listener.ChatToggleListener;
import fr.maxlego08.essentials.server.redis.listener.KickListener;
import fr.maxlego08.essentials.server.redis.listener.MessageListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RedisServer implements EssentialsServer, Listener {

    private final RedisSubscriberRunnable redisSubscriberRunnable;
    private final PlayerCache playerCache = new PlayerCache();
    private final UUID instanceId = UUID.randomUUID();
    private final EssentialsPlugin plugin;
    private final EssentialsUtils utils;
    private final String playersKey = "essentials:playerlist";
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
    public void sendMessage(UUID uuid, Message message, Object... objects) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) { // If player is online, send the message
            utils.message(uuid, message, objects);
            return;
        }

        sendMessage(new ServerMessage(ServerMessageType.SINGLE, uuid, null, message, objects));
    }

    @Override
    public void broadcastMessage(Permission permission, Message message, Object... objects) {

        this.utils.broadcast(permission, message, objects);
        sendMessage(new ServerMessage(ServerMessageType.BROADCAST_PERMISSION, null, permission, message, objects));
    }

    @Override
    public void broadcast(String message) {
        this.utils.broadcast(Message.COMMAND_CHAT_BROADCAST, "%message%", message);
        sendMessage(new ServerMessage(ServerMessageType.BROADCAST, null, null, Message.COMMAND_CHAT_BROADCAST, new String[]{"%message%", message}));
    }

    @Override
    public void sendPrivateMessage(User user, PrivateMessage privateMessage, String message) {
        // ToDO
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
        return jedisPool.getResource().sismember(playersKey, userName);
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
        String playerName = event.getPlayer().getName();
        this.playerCache.addPlayer(playerName);
        execute(jedis -> jedis.sadd(this.playersKey, playerName));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        this.playerCache.removePlayer(playerName);
        execute(jedis -> jedis.srem(this.playersKey, playerName));
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
}
