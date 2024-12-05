package fr.maxlego08.essentials.api.user;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.CooldownDTO;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.HomeDTO;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.dto.OptionDTO;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.dto.UserDTO;
import fr.maxlego08.essentials.api.dto.VoteSiteDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.utils.DynamicCooldown;
import fr.maxlego08.essentials.api.worldedit.Selection;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a user in the plugin.
 * Represents a user in the plugin.
 * This interface defines methods to interact with users, such as getting their UUID and name,
 * managing teleport requests, checking permissions, handling cooldowns, managing economy balances,
 * managing homes, and more.
 */
public interface User {

    /**
     * Gets the UUID of the user.
     *
     * @return The UUID of the user.
     */
    UUID getUniqueId();

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    String getName();

    /**
     * Sets the name of the user.
     *
     * @param name The new name of the user.
     */
    void setName(String name);

    /**
     * Sends a teleport request to another user.
     *
     * @param targetUser The user to send the teleport request to.
     */
    void sendTeleportRequest(User targetUser);

    /**
     * Cancels a teleport request sent to another user.
     *
     * @param targetUser The user to cancel the teleport request for.
     */
    void cancelTeleportRequest(User targetUser);

    /**
     * Gets the player associated with the user, if online.
     *
     * @return The player associated with the user, or null if the user is offline.
     */
    Player getPlayer();

    /**
     * Checks if the user is currently online.
     *
     * @return true if the user is online, false otherwise.
     */
    boolean isOnline();

    /**
     * Checks if the user is ignoring another user.
     *
     * @param uniqueId The UUID of the user to check for ignoring.
     * @return true if the user is ignoring the specified user, false otherwise.
     */
    boolean isIgnore(UUID uniqueId);

    /**
     * Gets the teleport request sent to the user.
     *
     * @return The teleport request sent to the user, or null if none.
     */
    TeleportRequest getTeleportRequest();

    /**
     * Sets the teleport request for the user.
     *
     * @param teleportRequest The teleport request to set for the user.
     */
    void setTeleportRequest(TeleportRequest teleportRequest);

    /**
     * Gets all teleport requests sent to the user.
     *
     * @return A collection of all teleport requests sent to the user.
     */
    Collection<TeleportRequest> getTeleportRequests();

    /**
     * Removes a teleport request sent to the user.
     *
     * @param user The user who sent the teleport request to remove.
     */
    void removeTeleportRequest(User user);

    /**
     * Teleports the user to the specified location immediately.
     *
     * @param location The location to teleport the user to.
     */
    void teleportNow(Location location);

    /**
     * Teleports the user to the specified location.
     *
     * @param location The location to teleport the user to.
     */
    void teleport(Location location);

    /**
     * Teleports the user to the specified location with messages.
     *
     * @param location       The location to teleport the user to.
     * @param message        The message to send before teleporting.
     * @param successMessage The message to send after successful teleportation.
     * @param args           Arguments to format into the messages.
     */
    void teleport(Location location, Message message, Message successMessage, Object... args);

    /**
     * Checks if the user has the specified permission.
     *
     * @param permission The permission to check.
     * @return true if the user has the permission, false otherwise.
     */
    boolean hasPermission(Permission permission);

    /**
     * Gets the user targeted by the current user.
     *
     * @return The user targeted by the current user.
     */
    User getTargetUser();

    /**
     * Sets the user targeted by the current user.
     *
     * @param user The user to target.
     */
    void setTargetUser(User user);

    /**
     * Checks if the user has the specified option enabled.
     *
     * @param option The option to check.
     * @return true if the user has the option enabled, false otherwise.
     */
    boolean getOption(Option option);

    /**
     * Sets the value of the specified option for the user.
     *
     * @param option The option to set.
     * @param value  The value to set for the option.
     */
    void setOption(Option option, boolean value);

    /**
     * Gets all options and their values for the user.
     *
     * @return A map containing all options and their values for the user.
     */
    Map<Option, Boolean> getOptions();

    /**
     * Sets the options for the user based on the provided option data transfer objects.
     *
     * @param options The option data transfer objects to set for the user.
     */
    void setOptions(List<OptionDTO> options);

    /**
     * Gets all cooldowns associated with the user.
     *
     * @return A map containing all cooldowns associated with the user.
     */
    Map<String, Long> getCooldowns();

    /**
     * Sets the cooldowns for the user based on the provided cooldown data transfer objects.
     *
     * @param cooldowns The cooldown data transfer objects to set for the user.
     */
    void setCooldowns(List<CooldownDTO> cooldowns);

    /**
     * Sets a cooldown for the user with the specified key and expiration timestamp.
     *
     * @param key       The key of the cooldown.
     * @param expiredAt The expiration timestamp of the cooldown.
     */
    void setCooldown(String key, long expiredAt);

    /**
     * Sets a cooldown for the user with the specified key and expiration timestamp without updating the database
     *
     * @param key       The key of the cooldown.
     * @param expiredAt The expiration timestamp of the cooldown.
     */
    void setCooldownSilent(String key, long expiredAt);

    /**
     * Checks if the user has a cooldown with the specified key.
     *
     * @param key The key of the cooldown to check.
     * @return true if the user has the cooldown, false otherwise.
     */
    boolean isCooldown(String key);

    /**
     * Gets the expiration timestamp of the cooldown with the specified key.
     *
     * @param key The key of the cooldown.
     * @return The expiration timestamp of the cooldown, or 0 if not found.
     */
    long getCooldown(String key);

    /**
     * Gets the remaining cooldown duration in seconds for the cooldown with the specified key.
     *
     * @param key The key of the cooldown.
     * @return The remaining cooldown duration in seconds.
     */
    long getCooldownSeconds(String key);

    /**
     * Adds a cooldown for the user with the specified key and duration in seconds.
     *
     * @param key     The key of the cooldown.
     * @param seconds The duration of the cooldown in seconds.
     */
    void addCooldown(String key, long seconds);

    /**
     * Gets the balance of the user in the specified economy.
     *
     * @param economy The economy to get the balance from.
     * @return The balance of the user in the specified economy.
     */
    BigDecimal getBalance(Economy economy);

    /**
     * Checks if the user has the specified amount of currency in the specified economy.
     *
     * @param economy    The economy to check.
     * @param bigDecimal The amount of currency to check.
     * @return true if the user has at least the specified amount of currency, false otherwise.
     */
    boolean has(Economy economy, BigDecimal bigDecimal);

    /**
     * Sets the balance of the user in the specified economy.
     *
     * @param economy    The economy to set the balance for.
     * @param bigDecimal The new balance of the user in the specified economy.
     */
    void set(Economy economy, BigDecimal bigDecimal);

    /**
     * Sets the balance of the user in the specified economy.
     *
     * @param economy    The economy to set the balance for.
     * @param bigDecimal The new balance of the user in the specified economy.
     * @param reason     The reason for the transaction.
     */
    void set(Economy economy, BigDecimal bigDecimal, String reason);

    /**
     * Withdraws currency from the user in the specified economy.
     *
     * @param economy    The economy to withdraw currency from.
     * @param bigDecimal The amount of currency to withdraw.
     */
    void withdraw(Economy economy, BigDecimal bigDecimal);

    /**
     * Withdraws currency from the user in the specified economy.
     *
     * @param economy    The economy to withdraw currency from.
     * @param bigDecimal The amount of currency to withdraw.
     * @param reason     The reason for the transaction.
     */
    void withdraw(Economy economy, BigDecimal bigDecimal, String reason);

    /**
     * Deposits currency to the user in the specified economy.
     *
     * @param economy    The economy to deposit currency to.
     * @param bigDecimal The amount of currency to deposit.
     */
    void deposit(Economy economy, BigDecimal bigDecimal);

    /**
     * Deposits currency to the user in the specified economy.
     *
     * @param economy    The economy to deposit currency to.
     * @param bigDecimal The amount of currency to deposit.
     * @param reason     The reason for the transaction.
     */
    void deposit(Economy economy, BigDecimal bigDecimal, String reason);

    /**
     * Sets the balance of the user from the specified source UUID in the specified economy.
     *
     * @param fromUuid   The UUID of the source account.
     * @param economy    The economy to set the balance for.
     * @param bigDecimal The new balance of the user in the specified economy.
     */
    void set(UUID fromUuid, Economy economy, BigDecimal bigDecimal);

    /**
     * Sets the balance of the user from the specified source UUID in the specified economy.
     *
     * @param fromUuid   The UUID of the source account.
     * @param economy    The economy to set the balance for.
     * @param bigDecimal The new balance of the user in the specified economy.
     * @param reason     The reason for the transaction.
     */
    void set(UUID fromUuid, Economy economy, BigDecimal bigDecimal, String reason);

    /**
     * Withdraws currency from the user from the specified source UUID in the specified economy.
     *
     * @param fromUuid   The UUID of the source account.
     * @param economy    The economy to withdraw currency from.
     * @param bigDecimal The amount of currency to withdraw.
     */
    void withdraw(UUID fromUuid, Economy economy, BigDecimal bigDecimal);

    /**
     * Withdraws currency from the user from the specified source UUID in the specified economy.
     *
     * @param fromUuid   The UUID of the source account.
     * @param economy    The economy to withdraw currency from.
     * @param bigDecimal The amount of currency to withdraw.
     * @param reason     The reason for the transaction.
     */
    void withdraw(UUID fromUuid, Economy economy, BigDecimal bigDecimal, String reason);

    /**
     * Deposits currency to the user from the specified source UUID in the specified economy.
     *
     * @param fromUuid   The UUID of the source account.
     * @param economy    The economy to deposit currency to.
     * @param bigDecimal The amount of currency to deposit.
     */
    void deposit(UUID fromUuid, Economy economy, BigDecimal bigDecimal);

    /**
     * Deposits currency to the user from the specified source UUID in the specified economy.
     *
     * @param fromUuid   The UUID of the source account.
     * @param economy    The economy to deposit currency to.
     * @param bigDecimal The amount of currency to deposit.
     * @param reason     The reason for the transaction.
     */
    void deposit(UUID fromUuid, Economy economy, BigDecimal bigDecimal, String reason);

    /**
     * Gets all economy balances of the user.
     *
     * @return A map containing all economy balances of the user.
     */
    Map<String, BigDecimal> getBalances();

    /**
     * Sets the balance of the user in the specified economy.
     *
     * @param key   The key of the economy.
     * @param value The new balance of the user in the specified economy.
     */
    void setBalance(String key, BigDecimal value);

    /**
     * Sets the economy balances of the user based on the provided economy data transfer objects.
     *
     * @param economyDTOS The economy data transfer objects to set for the user.
     */
    void setEconomies(List<EconomyDTO> economyDTOS);

    /**
     * Sets a target pay for the user, specifying the target user, economy, and amount.
     *
     * @param user       The user to pay.
     * @param economy    The economy to use for the payment.
     * @param bigDecimal The amount to pay.
     */
    void setTargetPay(User user, Economy economy, BigDecimal bigDecimal);

    /**
     * Gets the economy targeted for payment by the user.
     *
     * @return The economy targeted for payment, or null if none.
     */
    @Nullable Economy getTargetEconomy();


    /**
     * Gets the decimal value targeted for payment by the user.
     *
     * @return The decimal value targeted for payment, or null if none.
     */
    @Nullable BigDecimal getTargetDecimal();

    /**
     * Sets the last location of the user to their current location.
     * This method should be called to update the user's last location when needed.
     */
    void setLastLocation();

    /**
     * Gets the last known location of the user.
     *
     * @return The last known location of the user.
     */
    Location getLastLocation();

    /**
     * Sets the last location of the user.
     *
     * @param location The last location of the user.
     */
    void setLastLocation(Location location);

    /**
     * Checks if the user is joining the server for the first time.
     *
     * @return true if the user is joining for the first time, false otherwise.
     */
    boolean isFirstJoin();

    /**
     * Sets the user's first join status.
     * This method should be called to mark the user as having joined the server for the first time.
     */
    void setFirstJoin();

    /**
     * Sets a home location for the user with the specified name.
     *
     * @param name     The name of the home location.
     * @param location The location of the home.
     * @param confirm  Allows forcing the creation of a home even if another exists.
     */
    boolean setHome(String name, Location location, boolean confirm);

    /**
     * Gets the home location with the specified name for the user.
     *
     * @param name The name of the home location.
     * @return An optional containing the home location, or empty if not found.
     */
    Optional<Home> getHome(String name);

    /**
     * Gets all home locations set by the user.
     *
     * @return A list containing all home locations set by the user.
     */
    List<Home> getHomes();

    /**
     * Sets the home locations for the user based on the provided home data transfer objects.
     *
     * @param homeDTOS The home data transfer objects to set for the user.
     */
    void setHomes(List<HomeDTO> homeDTOS);

    /**
     * Gets the total number of home locations set by the user.
     *
     * @return The total number of home locations set by the user.
     */
    int countHomes();

    /**
     * Removes the home location with the specified name for the user.
     *
     * @param name The name of the home location to remove.
     */
    void removeHome(String name);

    /**
     * Checks if the user has a home location with the specified name.
     *
     * @param homeName The name of the home location to check.
     * @return true if the user has a home location with the specified name, false otherwise.
     */
    boolean isHomeName(String homeName);

    /**
     * Gets the active ban ID for the user.
     *
     * @return The active ban ID for the user.
     */
    int getActiveBanId();

    /**
     * Gets the active mute ID for the user.
     *
     * @return The active mute ID for the user.
     */
    int getActiveMuteId();

    /**
     * Sets the active ban and mute IDs for the user.
     *
     * @param banId  The active ban ID.
     * @param muteId The active mute ID.
     */
    void setSanction(Integer banId, Integer muteId);

    /**
     * Retrieves the current mute sanction.
     *
     * @return the current mute sanction
     */
    Sanction getMuteSanction();

    /**
     * Sets a mute sanction for the user.
     *
     * @param sanction the mute sanction to set
     */
    void setMuteSanction(Sanction sanction);

    /**
     * Checks if the user is muted.
     *
     * @return true if the user is muted, false otherwise
     */
    boolean isMute();

    /**
     * Sets a fake option with a specified value.
     *
     * @param option the option to set
     * @param value  the value to set for the option
     */
    void setFakeOption(Option option, boolean value);

    /**
     * Retrieves the list of fake sanctions.
     *
     * @return the list of fake sanctions
     */
    List<Sanction> getFakeSanctions();

    /**
     * Sets the list of fake sanctions.
     *
     * @param sanctions the list of sanctions to set
     */
    void setFakeSanctions(List<SanctionDTO> sanctions);

    /**
     * Retrieves the current ban sanction.
     *
     * @return the current ban sanction
     */
    Sanction getBanSanction();

    /**
     * Sets a ban sanction for the user.
     *
     * @param ban the ban sanction to set
     */
    void setBanSanction(Sanction ban);

    /**
     * Retrieves the last message sent by the user.
     *
     * @return the last message sent by the user
     */
    String getLastMessage();

    /**
     * Sets the last message sent by the user.
     *
     * @param message the message to set
     */
    void setLastMessage(String message);

    /**
     * Sets a private message for the user.
     *
     * @param uuid     the UUID of the other user
     * @param userName the name of the other user
     * @return the private message instance
     */
    PrivateMessage setPrivateMessage(UUID uuid, String userName);

    /**
     * Retrieves the current private message.
     *
     * @return the current private message
     */
    PrivateMessage getPrivateMessage();

    /**
     * Checks if the user has an ongoing private message conversation.
     *
     * @return true if the user has a private message, false otherwise
     */
    boolean hasPrivateMessage();

    /**
     * Retrieves the total playtime of the user.
     *
     * @return the total playtime in milliseconds
     */
    long getPlayTime();

    /**
     * Sets the total playtime of the user.
     *
     * @param playtime the playtime to set in milliseconds
     */
    void setPlayTime(long playtime);

    /**
     * Retrieves the playtime of the current session.
     *
     * @return the playtime of the current session in milliseconds
     */
    long getCurrentSessionPlayTime();

    /**
     * Starts tracking the playtime of the current session.
     */
    void startCurrentSessionPlayTime();

    /**
     * Retrieves the user's IP address.
     *
     * @return the IP address of the user
     */
    String getAddress();

    /**
     * Sets the user's IP address.
     *
     * @param address the IP address to set
     */
    void setAddress(String address);

    /**
     * Retrieves the cooldown time for a specific kit.
     *
     * @param kit the kit to check the cooldown for
     * @return the cooldown time in milliseconds
     */
    long getKitCooldown(Kit kit);

    /**
     * Checks if a specific kit is on cooldown for the user.
     *
     * @param kit the kit to check
     * @return true if the kit is on cooldown, false otherwise
     */
    boolean isKitCooldown(Kit kit);

    /**
     * Adds a cooldown to a specific kit.
     *
     * @param kit      the kit to add the cooldown to
     * @param cooldown the cooldown time in milliseconds
     */
    void addKitCooldown(Kit kit, long cooldown);

    /**
     * Retrieves the current kit preview.
     *
     * @return the current kit preview
     */
    Kit getKitPreview();

    /**
     * Opens the kit preview for a specific kit.
     *
     * @param kit the kit to preview
     */
    void openKitPreview(Kit kit);

    /**
     * Removes a specific cooldown by name.
     *
     * @param cooldownName the name of the cooldown to remove
     */
    void removeCooldown(String cooldownName);

    /**
     * Sets a power tool for a specific material with a command.
     *
     * @param type    the material to set the power tool for
     * @param command the command to assign to the power tool
     */
    void setPowerTools(Material type, String command);

    /**
     * Retrieves the map of power tools assigned to materials.
     *
     * @return the map of power tools
     */
    Map<Material, String> getPowerTools();

    /**
     * Sets the map of power tools assigned to materials.
     *
     * @param powerTools the map of power tools to set
     */
    void setPowerTools(Map<Material, String> powerTools);

    /**
     * Retrieves the power tool assigned to a specific material.
     *
     * @param material the material to get the power tool for
     * @return an {@link Optional} containing the command if found, or empty if not found
     */
    Optional<String> getPowerTool(Material material);

    /**
     * Deletes the power tool assigned to a specific material.
     *
     * @param material the material to delete the power tool for
     */
    void deletePowerTools(Material material);

    /**
     * Retrieves the list of mailbox items.
     *
     * @return the list of mailbox items
     */
    List<MailBoxItem> getMailBoxItems();

    /**
     * Sets the list of mailbox items.
     *
     * @param mailBoxItems the list of mailbox items to set
     */
    void setMailBoxItems(List<MailBoxDTO> mailBoxItems);

    /**
     * Adds an item to the mailbox.
     *
     * @param mailBoxItem the mailbox item to add
     */
    void addMailBoxItem(MailBoxItem mailBoxItem);

    /**
     * Retrieves the dynamic cooldown associated with the user.
     *
     * @return the {@link DynamicCooldown} instance
     */
    DynamicCooldown getDynamicCooldown();

    /**
     * Retrieves the current vote count for the user.
     *
     * @return the current vote count
     */
    long getVote();

    /**
     * Sets the current vote count for the user.
     *
     * @param amount the vote count to set
     */
    void setVote(long amount);

    /**
     * Adds to the current vote count for the user.
     *
     * @param amount the amount to add to the vote count
     */
    void addVote(long amount);

    /**
     * Removes from the current vote count for the user.
     *
     * @param amount the amount to remove from the vote count
     */
    void removeVote(long amount);

    /**
     * Sets the user data using a UserDTO object.
     *
     * @param userDTO the UserDTO object to use for setting user data
     */
    void setWithDTO(UserDTO userDTO);

    /**
     * Sets the vote site for the user.
     *
     * @param site the name of the vote site
     */
    void setVoteSite(String site);

    /**
     * Retrieves the last vote time for a specific site.
     *
     * @param site the name of the vote site
     * @return the last vote time in milliseconds
     */
    long getLastVoteSite(String site);

    /**
     * Retrieves the number of offline votes for the user.
     *
     * @return the number of offline votes
     */
    long getOfflineVotes();

    /**
     * Sets the list of vote sites for the user.
     *
     * @param sites the list of vote sites to set
     */
    void setVoteSites(List<VoteSiteDTO> sites);

    /**
     * Resets the offline votes for the user.
     */
    void resetOfflineVote();

    /**
     * Retrieves the current WorldEdit selection for the user.
     *
     * @return the {@link Selection} instance
     */
    Selection getSelection();

    /**
     * Checks if the user has an ongoing WorldEdit task.
     *
     * @return true if the user has a WorldEdit task, false otherwise
     */
    boolean hasWorldeditTask();

    /**
     * Retrieves the current WorldEdit task for the user.
     *
     * @return the {@link WorldEditTask} instance
     */
    WorldEditTask getWorldeditTask();

    /**
     * Sets the WorldEdit task for the user.
     *
     * @param worldEditTask the WorldEdit task to set
     */
    void setWorldeditTask(WorldEditTask worldEditTask);

    /**
     * Retrieves the item currently held in the user's main hand.
     *
     * @return the {@link ItemStack} in the user's main hand
     */
    ItemStack getItemInMainHand();

    /**
     * Sets the item to be held in the user's main hand.
     *
     * @param itemStack the {@link ItemStack} to set
     */
    void setItemInMainHand(ItemStack itemStack);

    /**
     * Plays a sound for the user.
     *
     * @param sound  the sound to play
     * @param volume the volume of the sound
     * @param pitch  the pitch of the sound
     */
    void playSound(Sound sound, float volume, float pitch);

    boolean isFrozen();

    void setFrozen(boolean b);

    Optional<Home> getCurrentDeleteHome();

    void setCurrentDeleteHome(Home home);

    long getFlySeconds();

    void setFlySeconds(long seconds);

    void addFlySeconds(long seconds);

    void removeFlySeconds(long seconds);
}
