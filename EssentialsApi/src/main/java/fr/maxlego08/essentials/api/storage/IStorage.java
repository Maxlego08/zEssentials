package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Consumer;

public interface IStorage {

    void onEnable();

    void onDisable();

    User createOrLoad(UUID uniqueId, String playerName);

    void onPlayerQuit(UUID uniqueId);

    User getUser(UUID uniqueId);

    void updateOption(UUID uniqueId, Option option, boolean value);

    void updateCooldown(UUID uniqueId, String key, long expiredAt);

    void updateEconomy(UUID uniqueId, Economy economy, BigDecimal bigDecimal);

    void updateUserMoney(UUID uniqueId, Consumer<User> consumer);
}
