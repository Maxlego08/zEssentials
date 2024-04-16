package fr.maxlego08.essentials.server.redis;

import com.google.gson.Gson;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.server.IdentifiedInstance;
import fr.maxlego08.essentials.api.server.ServerMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriberRunnable implements Runnable {

    public static final String messagesChannel = "essentials:messages";
    private final EssentialsPlugin plugin;
    private final RedisServer server;

    public RedisSubscriberRunnable(EssentialsPlugin plugin, RedisServer server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public void run() {

        try (Jedis jedis = this.server.getJedisPool().getResource()) {
            Gson gson = this.plugin.getGson();
            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    IdentifiedInstance instance = gson.fromJson(message, IdentifiedInstance.class);

                    if (instance == null) {
                        plugin.getLogger().severe("Server instance not found ! Message: " + message);
                        return;
                    }

                    if (instance.getInstanceId().equals(server.getInstanceId())) return;

                    switch (channel) {
                        case messagesChannel -> {
                            ServerMessage receivedMessage = gson.fromJson(message, ServerMessage.class);
                            server.getUtils().process(receivedMessage);
                        }
                    }
                }
            };

            jedis.subscribe(jedisPubSub, messagesChannel);
        } catch (Exception exception) {
            this.plugin.getLogger().severe("Failed to subscribe: " + exception.getMessage());
        }
    }
}
