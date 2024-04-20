package fr.maxlego08.essentials.server.redis.listener;

import fr.maxlego08.essentials.api.server.messages.ChatClear;
import fr.maxlego08.essentials.server.redis.RedisListener;
import fr.maxlego08.essentials.server.redis.RedisServer;

public class ChatClearListener extends RedisListener<ChatClear> {

    private final RedisServer redisServer;

    public ChatClearListener(RedisServer redisServer) {
        this.redisServer = redisServer;
    }

    @Override
    protected void onMessage(ChatClear message) {
        this.redisServer.clearLocalChat();
    }
}
