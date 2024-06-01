package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
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

        // Cooldowns
        placeholder.register("user_is_cooldown_", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return String.valueOf(user.isCooldown(key));
            } catch (Exception exception) {
                return "false";
            }
        }, "Returns true if the key is a cooldown", "cooldown key");

        placeholder.register("user_cooldown_second_", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return String.valueOf(user.getCooldownSeconds(key));
            } catch (Exception exception) {
                return "0";
            }
        }, "Returns the remaining time in seconds for the cooldown", "cooldown key");

        placeholder.register("user_cooldown_formatted_", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return TimerBuilder.getStringTime(user.getCooldown(key) - System.currentTimeMillis());
            } catch (Exception exception) {
                return "0s";
            }
        }, "Returns the remaining formatted time for the cooldown", "cooldown key");

        // Sanction
        placeholder.register("user_is_mute", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return String.valueOf(user.isMute());
        }, "Returns true if the player's is mute");

        placeholder.register("user_mute_seconds", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return String.valueOf((user.getMuteSanction().getDurationRemaining().toSeconds()));
            } catch (Exception exception) {
                return "0";
            }
        }, "Returns the remaining time in seconds for the mute");

        placeholder.register("user_mute_formatted", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return TimerBuilder.getStringTime(user.getMuteSanction().getDurationRemaining().toMillis());
            } catch (Exception exception) {
                return "0s";
            }
        }, "Returns the remaining formatted time for the mute");

        // Playtime

        placeholder.register("user_playtime_seconds", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return String.valueOf(user.getPlayTime() / 1000L);
            } catch (Exception exception) {
                return "0";
            }
        }, "Returns the playtime in seconds");

        placeholder.register("user_playtime_formatted", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return TimerBuilder.getStringTime(user.getPlayTime());
            } catch (Exception exception) {
                return "0s";
            }
        }, "Returns the formatted playtime ");

        // Session
        placeholder.register("user_session_seconds", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return String.valueOf(user.getCurrentSessionPlayTime() / 1000L);
            } catch (Exception exception) {
                return "0";
            }
        }, "Returns the current session in seconds");

        placeholder.register("user_session_formatted", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            try {
                return TimerBuilder.getStringTime(user.getCurrentSessionPlayTime());
            } catch (Exception exception) {
                return "0s";
            }
        }, "Returns the formatted current session");

        // Mailbox

        placeholder.register("user_mailbox_items", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return String.valueOf(user.getMailBoxItems().size());
        }, "Returns the number of items in the mailbox");
    }
}
