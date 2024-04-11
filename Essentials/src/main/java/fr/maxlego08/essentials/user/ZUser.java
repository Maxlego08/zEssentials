package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ZUser extends ZUtils implements User {

    private final EssentialsPlugin plugin;
    private final Map<UUID, TeleportRequest> teleports = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final UUID uniqueId;
    private final Map<Option, Boolean> options = new HashMap<>();
    private final Map<String, BigDecimal> balances = new HashMap<>();
    private String name;
    private TeleportRequest teleportRequest;
    private User targetUser;

    public ZUser(EssentialsPlugin plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
    }

    private IStorage getStorage() {
        return this.plugin.getStorageManager().getStorage();
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uniqueId);
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getOfflinePlayer(this.uniqueId).isOnline();
    }

    @Override
    public boolean isIgnore(UUID uniqueId) {
        return false;
    }

    @Override
    public void sendTeleportRequest(User targetUser) {

        if (targetUser == null || !targetUser.isOnline()) {
            message(this, Message.COMMAND_TPA_ERROR_SAME);
            return;
        }

        if (targetUser.getUniqueId().equals(this.uniqueId)) {
            message(this, Message.COMMAND_TPA_ERROR_SAME);
            return;
        }

        if (targetUser.isIgnore(this.uniqueId)) {
            message(this, Message.COMMAND_TELEPORT_IGNORE_PLAYER, targetUser);
            return;
        }

        this.teleports.entrySet().removeIf(next -> !next.getValue().isValid());

        if (this.teleports.containsKey(targetUser.getUniqueId())) {
            message(this, Message.COMMAND_TPA_ERROR, targetUser);
            return;
        }

        TeleportationModule teleportationModule = this.plugin.getModuleManager().getModule(TeleportationModule.class);
        long expired = System.currentTimeMillis() + (teleportationModule.getTeleportTpaExpire() * 1000L);
        TeleportRequest teleportRequest = new ZTeleportRequest(this.plugin, targetUser, this, expired);
        targetUser.setTeleportRequest(teleportRequest);
        this.teleports.put(targetUser.getUniqueId(), teleportRequest);

        message(this, Message.COMMAND_TPA_SENDER, targetUser);
        message(targetUser, Message.COMMAND_TPA_RECEIVER, getPlayer());
    }

    @Override
    public void cancelTeleportRequest(User targetUser) {

        if (!this.teleports.containsKey(targetUser.getUniqueId())) {
            message(this, Message.COMMAND_TP_CANCEL_ERROR, targetUser);
            return;
        }

        this.teleports.remove(targetUser.getUniqueId());

        if (targetUser.getTeleportRequest() != null && targetUser.getTeleportRequest().getFromUser() == this) {
            targetUser.setTeleportRequest(null);
        }

        message(this, Message.COMMAND_TP_CANCEL_SENDER, targetUser);
        message(targetUser, Message.COMMAND_TP_CANCEL_RECEIVER, this);
    }

    @Override
    public Collection<TeleportRequest> getTeleportRequests() {
        return this.teleports.values();
    }

    @Override
    public TeleportRequest getTeleportRequest() {
        return teleportRequest;
    }

    @Override
    public void setTeleportRequest(TeleportRequest teleportRequest) {
        this.teleportRequest = teleportRequest;
    }

    @Override
    public void removeTeleportRequest(User user) {
        this.teleports.remove(user.getUniqueId());
    }

    @Override
    public void teleport(Location location) {
        this.plugin.getScheduler().teleportAsync(this.getPlayer(), location);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return getPlayer().hasPermission(permission.asPermission());
    }

    @Override
    public User getTargetUser() {
        return targetUser;
    }

    @Override
    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    @Override
    public boolean getOption(Option option) {
        return options.getOrDefault(option, false);
    }

    @Override
    public void setOption(Option option, boolean value) {
        this.options.put(option, value);
        this.getStorage().updateOption(this.uniqueId, option, value);
    }

    @Override
    public Map<Option, Boolean> getOptions() {
        return this.options;
    }

    @Override
    public void setOptions(List<OptionDTO> options) {
        options.forEach((optionDTO) -> this.options.put(optionDTO.option_name(), optionDTO.option_value()));
    }

    @Override
    public Map<String, Long> getCooldowns() {
        long currentTime = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        return this.cooldowns;
    }

    @Override
    public void setCooldowns(List<CooldownDTO> cooldowns) {
        long currentTime = System.currentTimeMillis();
        cooldowns.stream().filter(cooldownDTO -> cooldownDTO.cooldown_value() > currentTime).forEach(cooldownDTO -> this.cooldowns.put(cooldownDTO.cooldown_name(), cooldownDTO.cooldown_value()));
    }

    @Override
    public void setCooldown(String key, long expiredAt) {
        this.cooldowns.put(key, expiredAt);
        this.getStorage().updateCooldown(this.uniqueId, key, expiredAt);
    }

    @Override
    public boolean isCooldown(String key) {
        return this.cooldowns.containsKey(key) && this.cooldowns.get(key) >= System.currentTimeMillis();
    }

    @Override
    public long getCooldown(String key) {
        return this.cooldowns.getOrDefault(key, 0L);
    }

    @Override
    public long getCooldownSeconds(String key) {
        long cooldown = getCooldown(key);
        return cooldown == 0 ? 0 : (cooldown - System.currentTimeMillis()) / 1000;
    }

    @Override
    public void addCooldown(String key, long seconds) {
        setCooldown(key, System.currentTimeMillis() + (1000L * seconds));
    }

    @Override
    public BigDecimal getBalance(Economy economy) {
        return this.balances.getOrDefault(economy.getName(), BigDecimal.ZERO);
    }

    @Override
    public boolean has(Economy economy, BigDecimal bigDecimal) {
        return bigDecimal.compareTo(getBalance(economy)) > 0;
    }

    @Override
    public void set(Economy economy, BigDecimal bigDecimal) {
        this.balances.put(economy.getName(), bigDecimal);
        getStorage().updateEconomy(this.uniqueId, economy, bigDecimal);
    }

    @Override
    public void withdraw(Economy economy, BigDecimal bigDecimal) {
        set(economy, getBalance(economy).subtract(bigDecimal));
    }

    @Override
    public void deposit(Economy economy, BigDecimal bigDecimal) {
        set(economy, getBalance(economy).add(bigDecimal));
    }

    @Override
    public Map<String, BigDecimal> getBalances() {
        return this.balances;
    }

    @Override
    public void setBalance(String key, BigDecimal value) {
        this.balances.put(key, value);
    }

    @Override
    public void setEconomies(List<EconomyDTO> economyDTOS) {
        economyDTOS.forEach(economyDTO -> this.balances.put(economyDTO.economy_name(), economyDTO.amount()));
    }
}
