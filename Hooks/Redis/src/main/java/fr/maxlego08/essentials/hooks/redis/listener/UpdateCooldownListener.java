package fr.maxlego08.essentials.hooks.redis.listener;

import fr.maxlego08.essentials.api.server.messages.UpdateCooldown;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.hooks.redis.RedisListener;

public class UpdateCooldownListener extends RedisListener<UpdateCooldown> {

    private final EssentialsUtils utils;

    public UpdateCooldownListener(EssentialsUtils utils) {
        this.utils = utils;
    }

    @Override
    protected void onMessage(UpdateCooldown message) {
        this.utils.updateCooldown(message.uniqueId(), message.cooldownName(), message.expiredAt());
    }
}
