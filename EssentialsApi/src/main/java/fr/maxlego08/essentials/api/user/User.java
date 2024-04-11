package fr.maxlego08.essentials.api.user;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    void teleport(Location location);

    boolean hasPermission(Permission permission);

    User getTargetUser();

    void setTargetUser(User user);

    boolean getOption(Option option);

    void setOption(Option option, boolean value);

    void setOptions(List<OptionDTO> options);

    Map<Option, Boolean> getOptions();

    Map<String, Long> getCooldowns();

    void setCooldown(String key, long expiredAt);

    void setCooldowns(List<CooldownDTO> cooldowns);

    boolean isCooldown(String key);

    long getCooldown(String key);

    long getCooldownSeconds(String key);

    void addCooldown(String key, long seconds);

    BigDecimal getBalance(Economy economy);

    boolean has(Economy economy, BigDecimal bigDecimal);

    void set(Economy economy, BigDecimal bigDecimal);

    void withdraw(Economy economy, BigDecimal bigDecimal);

    void deposit(Economy economy, BigDecimal bigDecimal);

    Map<String, BigDecimal> getBalances();

    void setBalance(String key, BigDecimal value);

    void setEconomies(List<EconomyDTO> economyDTOS);
}
