package fr.maxlego08.essentials.server.redis.listener;

import fr.maxlego08.essentials.api.server.messages.KickMessage;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.server.redis.RedisListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KickListener extends RedisListener<KickMessage> {

    private final EssentialsUtils essentialsUtils;

    public KickListener(EssentialsUtils essentialsUtils) {
        this.essentialsUtils = essentialsUtils;
    }

    @Override
    protected void onMessage(KickMessage message) {
        Player player = Bukkit.getPlayer(message.playerUniqueId());
        if (player != null) {
            this.essentialsUtils.kick(player, message.message(), message.arguments());
        }
    }
}
