package fr.maxlego08.essentials.api.user;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.messages.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface User {

    UUID getUniqueId();

    String getName();

    void setName(String name);

    void sendTeleportRequest(User targetUser);

    void cancelTeleportRequest(User targetUser);

    Player getPlayer();

    boolean isOnline();

    boolean isIgnore(UUID uniqueId);

    TeleportRequest getTeleportRequest();

    void setTeleportRequest(TeleportRequest teleportRequest);

    Collection<TeleportRequest> getTeleportRequests();

    void removeTeleportRequest(User user);

    void teleportNow(Location location);

    void teleport(Location location);

    void teleport(Location location, Message message, Message successMessage);

    boolean hasPermission(Permission permission);

    User getTargetUser();

    void setTargetUser(User user);

    boolean getOption(Option option);

    void setOption(Option option, boolean value);

    Map<Option, Boolean> getOptions();

    void setOptions(List<OptionDTO> options);

    Map<String, Long> getCooldowns();

    void setCooldowns(List<CooldownDTO> cooldowns);

    void setCooldown(String key, long expiredAt);

    boolean isCooldown(String key);

    long getCooldown(String key);

    long getCooldownSeconds(String key);

    void addCooldown(String key, long seconds);

    BigDecimal getBalance(Economy economy);

    boolean has(Economy economy, BigDecimal bigDecimal);

    void set(Economy economy, BigDecimal bigDecimal);

    void withdraw(Economy economy, BigDecimal bigDecimal);

    void deposit(Economy economy, BigDecimal bigDecimal);

    void set(UUID fromUuid, Economy economy, BigDecimal bigDecimal);

    void withdraw(UUID fromUuid, Economy economy, BigDecimal bigDecimal);

    void deposit(UUID fromUuid, Economy economy, BigDecimal bigDecimal);

    Map<String, BigDecimal> getBalances();

    void setBalance(String key, BigDecimal value);

    void setEconomies(List<EconomyDTO> economyDTOS);

    void setTargetPay(User user, Economy economy, BigDecimal bigDecimal);

    @Nullable Economy getTargetEconomy();

    @Nullable BigDecimal getTargetDecimal();

    void setLastLocation(Location location);

    void setLastLocation();

    Location getLastLocation();
}
