package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.database.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.SanctionDTO;
import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.api.database.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.user.UserRecord;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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

    void deleteCooldown(UUID uniqueId, String key);

    void updateUserMoney(UUID uniqueId, Consumer<User> consumer);

    void getUserEconomy(String userName, Consumer<List<EconomyDTO>> consumer);

    void fetchUniqueId(String userName, Consumer<UUID> consumer);

    void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount);

    long totalUsers();

    void upsertUser(User user);

    void upsertStorage(String key, Object value);

    void upsertHome(UUID uniqueId, Home home);

    void deleteHome(UUID uniqueId, String name);

    CompletableFuture<List<Home>> getHome(UUID uuid, String homeName);

    CompletionStage<List<Home>> getHomes(UUID uuid);

    void insertSanction(Sanction sanction, Consumer<Integer> consumer);

    void updateUserBan(UUID uuid, Integer index);

    void updateUserMute(UUID uuid, Integer index);

    boolean isBan(UUID uuid);

    Sanction getBan(UUID uuid);

    boolean isMute(UUID uuid);

    Sanction getMute(UUID uuid);

    List<SanctionDTO> getSanctions(UUID uuid);

    void insertChatMessage(UUID uuid, String content);

    List<ChatMessageDTO> getMessages(UUID targetUuid);

    Map<Option, Boolean> getOptions(UUID uuid);

    void insertCommand(UUID uuid, String command);

    void insertPlayTime(UUID uniqueId, long sessionPlayTime, long playtime, String address);

    UserRecord fetchUserRecord(UUID uuid);

    List<UserDTO> getUsers(String ip);

    List<CooldownDTO> getCooldowns(UUID uniqueId);

    void setPowerTools(UUID uniqueId, Material material, String command);

    void deletePowerTools(UUID uniqueId, Material material);

    void addMailBoxItem(MailBoxItem mailBoxItem);

    void removeMailBoxItem(int id);

    List<UserEconomyRankingDTO> getEconomyRanking(Economy economy);
}
