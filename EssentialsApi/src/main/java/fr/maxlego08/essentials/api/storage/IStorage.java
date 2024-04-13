package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.List;
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

    void getUserEconomy(String userName, Consumer<List<EconomyDTO>> consumer);

    void fetchUniqueId(String userName, Consumer<UUID> consumer);

    void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount);

    long totalUsers();

    void upsertUser(User user);

    void upsertStorage(String key, Object value);

    void upsertHome(UUID uniqueId, Home home);

    void deleteHome(UUID uniqueId, String name);
}
