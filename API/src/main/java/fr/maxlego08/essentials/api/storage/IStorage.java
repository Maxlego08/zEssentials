package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.dto.CooldownDTO;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.EconomyTransactionDTO;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.dto.PlayerSlotDTO;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.dto.UserDTO;
import fr.maxlego08.essentials.api.dto.UserEconomyDTO;
import fr.maxlego08.essentials.api.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.dto.UserVoteDTO;
import fr.maxlego08.essentials.api.dto.VaultDTO;
import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.user.UserRecord;
import fr.maxlego08.essentials.api.vault.Vault;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public interface IStorage {

    /**
     * Enables the storage system.
     */
    void onEnable();

    /**
     * Disables the storage system.
     */
    void onDisable();

    /**
     * Creates or loads a user with the given UUID and player name.
     *
     * @param uniqueId   the unique identifier of the user
     * @param playerName the name of the player
     * @return the loaded or newly created user
     */
    User createOrLoad(UUID uniqueId, String playerName);

    /**
     * Handles actions when a player quits.
     *
     * @param uniqueId the unique identifier of the user
     */
    void onPlayerQuit(UUID uniqueId);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param uniqueId the unique identifier of the user
     * @return the user associated with the given unique identifier
     */
    User getUser(UUID uniqueId);

    /**
     * Updates a user's option.
     *
     * @param uniqueId the unique identifier of the user
     * @param option   the option to update
     * @param value    the new value of the option
     */
    void updateOption(UUID uniqueId, Option option, boolean value);

    /**
     * Updates a user's cooldown.
     *
     * @param uniqueId  the unique identifier of the user
     * @param key       the key representing the cooldown
     * @param expiredAt the expiration time of the cooldown
     */
    void updateCooldown(UUID uniqueId, String key, long expiredAt);

    /**
     * Updates a user's economy data.
     *
     * @param uniqueId   the unique identifier of the user
     * @param economy    the economy to update
     * @param bigDecimal the new value of the economy
     */
    void updateEconomy(UUID uniqueId, Economy economy, BigDecimal bigDecimal);

    /**
     * Deletes a user's cooldown.
     *
     * @param uniqueId the unique identifier of the user
     * @param key      the key representing the cooldown
     */
    void deleteCooldown(UUID uniqueId, String key);

    /**
     * Updates a user's money asynchronously.
     *
     * @param uniqueId the unique identifier of the user
     * @param consumer the consumer to apply to the user
     */
    void updateUserMoney(UUID uniqueId, Consumer<User> consumer);

    /**
     * Retrieves the economy data of a user by their name.
     *
     * @param userName the name of the user
     * @param consumer the consumer to apply to the economy data
     */
    void getUserEconomy(String userName, Consumer<List<EconomyDTO>> consumer);

    /**
     * Fetches a user's unique identifier by their name.
     *
     * @param userName the name of the user
     * @param consumer the consumer to apply to the fetched UUID
     */
    void fetchUniqueId(String userName, Consumer<UUID> consumer);

    /**
     * Stores a transaction between two users.
     *
     * @param fromUuid   the UUID of the sender
     * @param toUuid     the UUID of the receiver
     * @param economy    the economy type involved
     * @param fromAmount the amount from the sender
     * @param toAmount   the amount to the receiver
     * @param reason     the reason for the transaction
     */
    void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount, String reason);

    /**
     * Retrieves transactions for a specific user and economy.
     *
     * @param toUuid  the UUID of the receiver
     * @param economy the economy type involved
     * @return a list of economy transactions
     */
    List<EconomyTransactionDTO> getTransactions(UUID toUuid, Economy economy);

    /**
     * Retrieves the total number of users.
     *
     * @return the total number of users
     */
    long totalUsers();

    /**
     * Inserts or updates a user.
     *
     * @param user the user to upsert
     */
    void upsertUser(User user);

    /**
     * Inserts or updates a storage entry.
     *
     * @param key   the storage key
     * @param value the storage value
     */
    void upsertStorage(String key, Object value);

    /**
     * Inserts or updates a home for a user.
     *
     * @param uniqueId the UUID of the user
     * @param home     the home to upsert
     */
    void upsertHome(UUID uniqueId, Home home);

    /**
     * Deletes a home for a user.
     *
     * @param uniqueId the UUID of the user
     * @param name     the name of the home
     */
    void deleteHome(UUID uniqueId, String name);

    /**
     * Retrieves a specific home for a user.
     *
     * @param uuid     the UUID of the user
     * @param homeName the name of the home
     * @return a CompletableFuture with a list of homes
     */
    CompletableFuture<List<Home>> getHome(UUID uuid, String homeName);

    /**
     * Retrieves all homes for a user.
     *
     * @param uuid the UUID of the user
     * @return a CompletionStage with a list of homes
     */
    CompletionStage<List<Home>> getHomes(UUID uuid);

    /**
     * Inserts a sanction for a user.
     *
     * @param sanction the sanction to insert
     * @param consumer the consumer to apply to the sanction ID
     */
    void insertSanction(Sanction sanction, Consumer<Integer> consumer);

    /**
     * Updates a user's ban status.
     *
     * @param uuid  the UUID of the user
     * @param index the index of the ban
     */
    void updateUserBan(UUID uuid, Integer index);

    /**
     * Updates a user's mute status.
     *
     * @param uuid  the UUID of the user
     * @param index the index of the mute
     */
    void updateUserMute(UUID uuid, Integer index);

    /**
     * Checks if a user is banned.
     *
     * @param uuid the UUID of the user
     * @return true if the user is banned, otherwise false
     */
    boolean isBan(UUID uuid);

    /**
     * Retrieves the ban sanction for a user.
     *
     * @param uuid the UUID of the user
     * @return the ban sanction
     */
    Sanction getBan(UUID uuid);

    /**
     * Checks if a user is muted.
     *
     * @param uuid the UUID of the user
     * @return true if the user is muted, otherwise false
     */
    boolean isMute(UUID uuid);

    /**
     * Retrieves the mute sanction for a user.
     *
     * @param uuid the UUID of the user
     * @return the mute sanction
     */
    Sanction getMute(UUID uuid);

    /**
     * Retrieves sanctions for a user.
     *
     * @param uuid the UUID of the user
     * @return a list of sanctions
     */
    List<SanctionDTO> getSanctions(UUID uuid);

    /**
     * Inserts a chat message from a user.
     *
     * @param uuid    the UUID of the user
     * @param content the chat message content
     */
    void insertChatMessage(UUID uuid, String content);

    /**
     * Retrieves chat messages from a user.
     *
     * @param targetUuid the UUID of the user
     * @return a list of chat messages
     */
    List<ChatMessageDTO> getMessages(UUID targetUuid);

    /**
     * Retrieves options for a user.
     *
     * @param uuid the UUID of the user
     * @return a map of options
     */
    Map<Option, Boolean> getOptions(UUID uuid);

    /**
     * Inserts a command from a user.
     *
     * @param uuid    the UUID of the user
     * @param command the command
     */
    void insertCommand(UUID uuid, String command);

    /**
     * Inserts play time for a user.
     *
     * @param uniqueId        the UUID of the user
     * @param sessionPlayTime the play time for the current session
     * @param playtime        the play time
     * @param address         the address of the user
     */
    void insertPlayTime(UUID uniqueId, long sessionPlayTime, long playtime, String address);

    /**
     * Retrieves a user record.
     *
     * @param uuid the UUID of the user
     * @return the user record
     */
    UserRecord fetchUserRecord(UUID uuid);

    /**
     * Retrieves users with a specific IP.
     *
     * @param ip the IP
     * @return a list of users
     */
    List<UserDTO> getUsers(String ip);

    /**
     * Retrieves cooldowns for a user.
     *
     * @param uniqueId the UUID of the user
     * @return a list of cooldowns
     */
    List<CooldownDTO> getCooldowns(UUID uniqueId);

    /**
     * Sets power tools for a user.
     *
     * @param uniqueId the UUID of the user
     * @param material the material
     * @param command  the command
     */
    void setPowerTools(UUID uniqueId, Material material, String command);

    /**
     * Deletes power tools for a user.
     *
     * @param uniqueId the UUID of the user
     * @param material the material
     */
    void deletePowerTools(UUID uniqueId, Material material);

    /**
     * Adds a mail box item.
     *
     * @param mailBoxItem the mail box item
     */
    void addMailBoxItem(MailBoxItem mailBoxItem);

    /**
     * Removes a mail box item.
     *
     * @param id the ID of the mail box item
     */
    void removeMailBoxItem(int id);

    /**
     * Retrieves economy rankings.
     *
     * @param economy the economy
     * @return a list of economy rankings
     */
    List<UserEconomyRankingDTO> getEconomyRanking(Economy economy);

    /**
     * Retrieves mail box items.
     *
     * @param uniqueId the UUID of the user
     * @return a list of mail box items
     */
    List<MailBoxDTO> getMailBox(UUID uniqueId);

    /**
     * Fetches offline player economies.
     *
     * @param consumer the consumer
     */
    void fetchOfflinePlayerEconomies(Consumer<List<UserEconomyDTO>> consumer);

    /**
     * Sets votes for a user.
     *
     * @param uniqueId     the UUID of the user
     * @param vote         the vote
     * @param offline_vote the offline vote
     */
    void setVote(UUID uniqueId, long vote, long offline_vote);

    /**
     * Retrieves a user vote.
     *
     * @param uniqueId the UUID of the user
     * @return the user vote
     */
    UserVoteDTO getVote(UUID uniqueId);

    /**
     * Updates server storage.
     *
     * @param key    the key
     * @param object the object
     */
    void updateServerStorage(String key, Object object);

    /**
     * Sets the last vote for a user.
     *
     * @param uniqueId the UUID of the user
     * @param site     the site
     */
    void setLastVote(UUID uniqueId, String site);

    /**
     * Resets votes.
     */
    void resetVotes();

    /**
     * Updates the quantity of a vault item.
     *
     * @param uniqueId the UUID of the user
     * @param vaultId  the vault ID
     * @param slot     the slot
     * @param quantity the quantity
     */
    void updateVaultQuantity(UUID uniqueId, int vaultId, int slot, long quantity);

    /**
     * Removes a vault item.
     *
     * @param uniqueId the UUID of the user
     * @param vaultId  the vault ID
     * @param slot     the slot
     */
    void removeVaultItem(UUID uniqueId, int vaultId, int slot);

    /**
     * Creates a vault item.
     *
     * @param uniqueId the UUID of the user
     * @param vaultId  the vault ID
     * @param slot     the slot
     * @param quantity the quantity
     * @param item     the item
     */
    void createVaultItem(UUID uniqueId, int vaultId, int slot, long quantity, String item);

    /**
     * Sets the vault slot.
     *
     * @param uniqueId the UUID of the user
     * @param slots    the slots
     */
    void setVaultSlot(UUID uniqueId, int slots);

    /**
     * Retrieves the vaults.
     *
     * @return the vaults
     */
    List<VaultDTO> getVaults();

    /**
     * Retrieves the vault items.
     *
     * @return the vault items
     */
    List<VaultItemDTO> getVaultItems();

    /**
     * Retrieves the player vault slots.
     *
     * @return the player vault slots
     */
    List<PlayerSlotDTO> getPlayerVaultSlots();

    /**
     * Updates a vault.
     *
     * @param uniqueId the UUID of the user
     * @param vault    the vault
     */
    void updateVault(UUID uniqueId, Vault vault);

    /**
     * Updates a user frozen.
     *
     * @param uuid   the UUID of the user
     * @param frozen the frozen
     */
    void updateUserFrozen(UUID uuid, boolean frozen);

    /**
     * Upserts the fly seconds.
     *
     * @param uniqueId   the UUID of the user
     * @param flySeconds the fly seconds
     */
    void upsertFlySeconds(UUID uniqueId, long flySeconds);

    /**
     * Retrieves the fly seconds.
     *
     * @param uniqueId the UUID of the user
     * @return the fly seconds
     */
    long getFlySeconds(UUID uniqueId);

    /**
     * Deletes world data.
     *
     * @param worldName the world name
     */
    void deleteWorldData(String worldName);
}