package fr.maxlego08.essentials.server.redis;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.server.PlayerCache;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerMessage;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisServer implements EssentialsServer, Listener {

    private final PlayerCache playerCache = new PlayerCache();
    private final UUID instanceId = UUID.randomUUID();
    private final EssentialsPlugin plugin;
    private final EssentialsUtils utils;
    private final String playersKey = "essentials:playerlist";
    private JedisPool jedisPool;

    public RedisServer(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
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

        Thread thread = new Thread(new RedisSubscriberRunnable(this.plugin, this));
        thread.start();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);

        // Fetch players name every minute
        plugin.getScheduler().runTimerAsync(() -> {
            System.out.println("UPDATE BEFORE " + this.getPlayersNames());
            this.playerCache.setPlayers(jedisPool.getResource().smembers(this.playersKey));
            System.out.println("UPDATE AFTER " + this.getPlayersNames());
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
        if (this.jedisPool != null) {
            this.jedisPool.close();
        }
    }

    @Override
    public List<String> getPlayersNames() {
        return new ArrayList<>(this.playerCache.getPlayers());
    }

    @Override
    public void sendMessage(Player player, Message message, Object... objects) {

        utils.message(player, message, objects);

        ServerMessage serverMessage = new ServerMessage(this.instanceId, ServerMessageType.SINGLE, player.getUniqueId(), null, message, objects);
        sendServerMessage(serverMessage);
    }

    @Override
    public void broadcastMessage(Permission permission, Message message, Object... objects) {

        utils.broadcast(permission, message, objects);

        ServerMessage serverMessage = new ServerMessage(this.instanceId, ServerMessageType.BROADCAST, null, permission, message, objects);
        sendServerMessage(serverMessage);
    }

    private void sendServerMessage(ServerMessage serverMessage) {
        this.plugin.getScheduler().runAsync(wrappedTask -> {
            String jsonMessage = this.plugin.getGson().toJson(serverMessage);
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
        System.out.println("JE JOIN " + playerName);
        this.playerCache.addPlayer(playerName);
        this.plugin.getScheduler().runAsync(wrappedTask -> jedisPool.getResource().sadd(this.playersKey, playerName));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        System.out.println("JE leave " + playerName);
        this.playerCache.removePlayer(playerName);
        this.plugin.getScheduler().runAsync(wrappedTask -> jedisPool.getResource().srem(this.playersKey, playerName));
    }
}
