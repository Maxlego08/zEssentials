package fr.maxlego08.essentials.hooks.redis.listener;

import fr.maxlego08.essentials.api.server.messages.ChatClear;
import fr.maxlego08.essentials.hooks.redis.RedisListener;
import fr.maxlego08.essentials.hooks.redis.RedisServer;

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
