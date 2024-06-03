package fr.maxlego08.essentials.hooks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.storage.IStorage;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VaultEconomy implements Economy {

    private final EssentialsPlugin essentialsPlugin;

    public VaultEconomy(EssentialsPlugin essentialsPlugin) {
        this.essentialsPlugin = essentialsPlugin;
        Bukkit.getServer().getServicesManager().register(Economy.class, this, this.essentialsPlugin, ServicePriority.Normal);
    }

    private fr.maxlego08.essentials.api.economy.Economy getEconomy() {
        return this.essentialsPlugin.getEconomyManager().getVaultEconomy();
    }

    @Override
    public boolean isEnabled() {
        return essentialsPlugin.isEconomyEnable();
    }

    @Override
    public String getName() {
        return "zEssentials Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return this.essentialsPlugin.getEconomyManager().format(getEconomy(), amount);
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return getEconomy().getSymbol();
    }

    @Override
    public boolean hasAccount(String playerName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    @Override
    public double getBalance(String playerName) {
        this.essentialsPlugin.getLogger().severe("Player Name not supported for getBalance !");
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.essentialsPlugin.getStorageManager().getStorage().getUser(player.getUniqueId()).getBalance(getEconomy()).doubleValue();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {

        IStorage iStorage = this.essentialsPlugin.getStorageManager().getStorage();
        EconomyManager economyManager = this.essentialsPlugin.getEconomyManager();
        iStorage.fetchUniqueId(playerName, uuid -> {
            if (uuid == null) return;
            economyManager.withdraw(uuid, getEconomy(), new BigDecimal(amount));
        });

        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.SUCCESS, "Yeah its work (i guess its async withdraw so idk maybe) !");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {

        EconomyManager economyManager = this.essentialsPlugin.getEconomyManager();
        economyManager.withdraw(player.getUniqueId(), getEconomy(), new BigDecimal(amount));
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.SUCCESS, "Yeah its work (i guess its async withdraw so idk maybe) !");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {

        IStorage iStorage = this.essentialsPlugin.getStorageManager().getStorage();
        EconomyManager economyManager = this.essentialsPlugin.getEconomyManager();
        iStorage.fetchUniqueId(playerName, uuid -> {
            if (uuid == null) return;
            economyManager.deposit(uuid, getEconomy(), new BigDecimal(amount));
        });

        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.SUCCESS, "Yeah its work (i guess its async deposit so idk maybe) !");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {

        EconomyManager economyManager = this.essentialsPlugin.getEconomyManager();
        economyManager.deposit(player.getUniqueId(), getEconomy(), new BigDecimal(amount));
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.SUCCESS, "Yeah its work (i guess its async deposit so idk maybe) !");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "no bank");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
