package fr.maxlego08.essentials.server.redis.listener;

import fr.maxlego08.essentials.api.server.messages.ChatToggle;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.server.redis.RedisListener;

public class ChatToggleListener extends RedisListener<ChatToggle> {

    private final EssentialsUtils utils;

    public ChatToggleListener(EssentialsUtils utils) {
        this.utils = utils;
    }

    @Override
    protected void onMessage(ChatToggle message) {
        this.utils.toggleChat(message.toggle());
    }
}
