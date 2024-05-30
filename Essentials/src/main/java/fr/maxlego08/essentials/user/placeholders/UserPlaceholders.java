package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.math.BigDecimal;
import java.util.Optional;

public class UserPlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        IStorage iStorage = plugin.getStorageManager().getStorage();
        EconomyManager economyManager = plugin.getEconomyManager();

        // Target

        placeholder.register("user_target_player_name", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user.getTargetUser() != null ? user.getTargetUser().getName() : "no";
        }, "Returns the name of the target player");

        placeholder.register("user_target_is_ban", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user.getTargetUser() != null ? String.valueOf(user.getTargetUser().getOption(Option.BAN)) : "false";
        }, "Returns true if the target player is banned, otherwise false");

        placeholder.register("user_target_is_mute", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user.getTargetUser() != null ? String.valueOf(user.getTargetUser().getOption(Option.MUTE)) : "false";
        }, "Returns true if the target player is muted, otherwise false");

        placeholder.register("user_target_pay_amount", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            Economy economy = user.getTargetEconomy();
            BigDecimal decimal = user.getTargetDecimal();
            return economy == null || decimal == null ? "0" : economyManager.format(economy, decimal);
        }, "Returns the number formatted for the /pay command");

        // Balance

        placeholder.register("user_formatted_balance_", (player, args) -> {
            User user = iStorage.getUser(player.getUniqueId());
            Optional<Economy> optional = economyManager.getEconomy(args);
            if (optional.isEmpty()) {
                return "Economy " + args + " was not found";
            }
            Economy economy = optional.get();
            BigDecimal decimal = user.getBalance(economy);
            return decimal == null ? "0" : economyManager.format(economy, decimal);
        }, "Returns the formatted number for a given economy", "economy");

        placeholder.register("user_balance_", (player, args) -> {
            User user = iStorage.getUser(player.getUniqueId());
            Optional<Economy> optional = economyManager.getEconomy(args);
            if (optional.isEmpty()) {
                return "Economy " + args + " was not found";
            }
            Economy economy = optional.get();
            return user.getBalance(economy).toString();
        }, "Returns the number for a given economy", "economy");

        placeholder.register("user_option_", (player, args) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                Option option = Option.valueOf(args.toUpperCase());
                return String.valueOf(user.getOption(option));
            } catch (Exception exception) {
                return "Option " + args + " was not found";
            }
        }, "Returns the value for an option", "option name");

        placeholder.register("user_position_", (player, economyName) -> String.valueOf(economyManager.getUserPosition(economyName, player.getUniqueId())), "Returns the player's position in baltop for a given economy", "economy name");
    }
}
