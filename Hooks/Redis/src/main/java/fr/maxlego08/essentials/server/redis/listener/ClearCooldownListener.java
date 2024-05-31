package fr.maxlego08.essentials.server.redis.listener;

import fr.maxlego08.essentials.api.server.messages.ClearCooldown;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.server.redis.RedisListener;

public class ClearCooldownListener extends RedisListener<ClearCooldown> {

    private final EssentialsUtils utils;

    public ClearCooldownListener(EssentialsUtils utils) {
        this.utils = utils;
    }

    @Override
    protected void onMessage(ClearCooldown message) {
        this.utils.deleteCooldown(message.uniqueId(), message.cooldownName());
    }
}
