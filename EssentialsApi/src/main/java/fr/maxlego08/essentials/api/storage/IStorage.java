package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;

import java.util.UUID;

public interface IStorage {

    void onEnable();

    void onDisable();

    User createOrLoad(UUID uniqueId, String playerName);

    void onPlayerQuit(UUID uniqueId);

    User getUser(UUID uniqueId);

    void updateOption(UUID uniqueId, Option option, boolean value);

    void updateCooldown(UUID uniqueId, String key, long expiredAt);
}
