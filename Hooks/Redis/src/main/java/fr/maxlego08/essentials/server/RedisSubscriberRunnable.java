package fr.maxlego08.essentials.server;

import com.google.gson.Gson;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RedisSubscriberRunnable implements Runnable {

    public static final String messagesChannel = "essentials:messages";
    private final EssentialsPlugin plugin;
    private final RedisServer server;
    private final Map<Class<?>, RedisListener<?>> listeners = new HashMap<>();

    public RedisSubscriberRunnable(EssentialsPlugin plugin, RedisServer server) {
        this.plugin = plugin;
        this.server = server;
    }

    public <T> void registerListener(Class<T> messageClass, RedisListener<T> listener) {
        listeners.put(messageClass, listener);
    }

    private RedisListener<?> getListener(Class<?> messageClass) {
        return listeners.get(messageClass);
    }


    @Override
    public void run() {

        try (Jedis jedis = this.server.getJedisPool().getResource()) {
            Gson gson = this.plugin.getGson();
            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {

                    plugin.debug("Receive: " + message + " from " + channel);

                    if (!Objects.equals(channel, messagesChannel)) return;

                    RedisMessage<?> redisMessage = gson.fromJson(message, RedisMessage.class);
                    if (redisMessage == null) {
                        plugin.getLogger().severe("Server instance not found ! Message: " + message);
                        return;
                    }

                    if (redisMessage.serverId().equals(server.getInstanceId())) return;

                    Map<?, ?> map = (Map<?, ?>) redisMessage.t();

                    plugin.debug("Map: " + map);

                    try {
                        Class<?> messageClass = Class.forName(redisMessage.className());
                        RedisListener<?> listener = getListener(messageClass);
                        Object result = plugin.getUtils().createInstanceFromMap(messageClass.getConstructors()[0], map);
                        plugin.debug("MessageClass: " + messageClass);
                        plugin.debug("Listener: " + listener);
                        plugin.debug("Result: " + result);

                        if (listener != null) {
                            listener.message(result);
                        }

                    } catch (ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            };

            jedis.subscribe(jedisPubSub, messagesChannel);
        } catch (Exception exception) {
            this.plugin.getLogger().severe("Failed to subscribe: " + exception.getMessage());
        }
    }
}
