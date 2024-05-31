package fr.maxlego08.essentials.server.redis.listener;

import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.server.redis.RedisListener;

public class MessageListener extends RedisListener<ServerMessage> {

    private final EssentialsUtils utils;

    public MessageListener(EssentialsUtils utils) {
        this.utils = utils;
    }

    @Override
    protected void onMessage(ServerMessage message) {
        this.utils.process(message);
    }
}
