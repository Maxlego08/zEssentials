package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.economy.PriceFormat;
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
        var afkManager = plugin.getAfkManager();

        // Target

        placeholder.register("user_target_player_name", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null && user.getTargetUser() != null ? user.getTargetUser().getName() : "no";
        }, "Returns the name of the target player");

        placeholder.register("user_target_is_ban", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null && user.getTargetUser() != null ? String.valueOf(user.getTargetUser().getOption(Option.BAN)) : "false";
        }, "Returns true if the target player is banned, otherwise false");

        placeholder.register("user_target_is_mute", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null && user.getTargetUser() != null ? String.valueOf(user.getTargetUser().getOption(Option.MUTE)) : "false";
        }, "Returns true if the target player is muted, otherwise false");

        placeholder.register("user_target_pay_amount", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            if (user == null) return "0";
            Economy economy = user.getTargetEconomy();
            BigDecimal decimal = user.getTargetDecimal();
            return economy == null || decimal == null ? "0" : economyManager.format(economy, decimal);
        }, "Returns the number formatted for the /pay command");

        // Balance

        placeholder.register("user_formatted_balance_", (player, args) -> {
            User user = iStorage.getUser(player.getUniqueId());
            if (user == null) return "0";
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
            if (user == null) return "0";
            Optional<Economy> optional = economyManager.getEconomy(args);
            if (optional.isEmpty()) {
                return "Economy " + args + " was not found";
            }
            Economy economy = optional.get();
            return user.getBalance(economy).toString();
        }, "Returns the number for a given economy", "economy");

        placeholder.register("user_custom_balance_", (player, args) -> {
            User user = iStorage.getUser(player.getUniqueId());
            if (user == null) return "0";
            String[] split = args.split("_", 2);
            if (split.length != 2) return "Error: not enough arguments";

            Optional<Economy> optional = economyManager.getEconomy(split[0]);
            if (optional.isEmpty()) {
                return "Economy " + args + " was not found";
            }
            try {
                PriceFormat priceFormat = PriceFormat.valueOf(split[1].toUpperCase());
                Economy economy = optional.get();
                return economyManager.format(priceFormat, user.getBalance(economy));
            } catch (Exception exception) {
                return "Format " + split[1] + " was not found";
            }
        }, "Returns the custom formatted number for a given economy", "economy", "format");

        // Option

        placeholder.register("user_option_", (player, args) -> {
            User user = iStorage.getUser(player.getUniqueId());
            if (user == null) return "false";
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
            return user == null ? "false" : String.valueOf(user.isCooldown(key));
        }, "Returns true if the key is a cooldown", "cooldown key");

        placeholder.register("user_cooldown_second_", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user == null ? "0" : String.valueOf(user.getCooldownSeconds(key));
        }, "Returns the remaining time in seconds for the cooldown", "cooldown key");

        placeholder.register("user_cooldown_formatted_", (player, key) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return TimerBuilder.getStringTime(user == null ? 0 : (user.getCooldown(key) - System.currentTimeMillis()));
        }, "Returns the remaining formatted time for the cooldown", "cooldown key");

        // Sanction
        placeholder.register("user_is_mute", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null ? String.valueOf(user.isMute()) : "false";
        }, "Returns true if the player's is mute");

        placeholder.register("user_mute_seconds", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null ? String.valueOf((user.getMuteSanction().getDurationRemaining().toSeconds())) : "0";
        }, "Returns the remaining time in seconds for the mute");

        placeholder.register("user_mute_formatted", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return TimerBuilder.getStringTime(user != null ? user.getMuteSanction().getDurationRemaining().toMillis() : 0);
        }, "Returns the remaining formatted time for the mute");

        // Mailbox

        placeholder.register("user_mailbox_items", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user == null ? "0" : String.valueOf(user.getMailBoxItems().size());
        }, "Returns the number of items in the mailbox");

        // God
        placeholder.register("user_is_god", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user == null ? "false" : String.valueOf(user.getOption(Option.GOD));
        }, "Returns the true if user is in god mode");

        placeholder.register("user_is_afk", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user != null && user.isAfk() ? "true" : "false";
        }, "Returns true if the player is AFK");

        placeholder.register("user_afk_status", player -> {
            User user = iStorage.getUser(player.getUniqueId());
            boolean isAfk = user != null && user.isAfk();
            if (afkManager == null) {
                return isAfk ? "&aV" : "&cX";
            }
            return isAfk ? afkManager.getPlaceholderAfk() : afkManager.getPlaceholderNotAfk();
        }, "Returns the configured placeholder for the player's AFK status");

        // Fly
        placeholder.register("user_fly_seconds", (player) -> {
            User user = iStorage.getUser(player.getUniqueId());
            return user == null ? "0" : String.valueOf(user.getFlySeconds());
        }, "Returns the number of seconds for temporary fly");

        // Repair all
        placeholder.register("can_repair_all", (player) -> countRepairItems(player.getInventory()) > 0 ? "true" : "false", "Returns true if the player can repair all of their items");

        placeholder.register("count_repair_all", (player) -> String.valueOf(countRepairItems(player.getInventory())), "Returns the number of items that the player can repair");
        placeholder.register("user_world", (player) -> player.getWorld().getName(), "Returns the name of the world the player is currently in");
        placeholder.register("user_x", (player) -> String.valueOf(player.getLocation().getX()), "Returns the x coordinate of the player");
        placeholder.register("user_y", (player) -> String.valueOf(player.getLocation().getY()), "Returns the y coordinate of the player");
        placeholder.register("user_z", (player) -> String.valueOf(player.getLocation().getZ()), "Returns the z coordinate of the player");
        placeholder.register("user_block_x", (player) -> String.valueOf(player.getLocation().getBlockX()), "Returns the block x coordinate of the player");
        placeholder.register("user_block_y", (player) -> String.valueOf(player.getLocation().getBlockY()), "Returns the block y coordinate of the player");
        placeholder.register("user_block_z", (player) -> String.valueOf(player.getLocation().getBlockZ()), "Returns the block z coordinate of the player");
        placeholder.register("user_biome", (player) -> player.getWorld().getBiome(player.getLocation()).name(), "Returns the biome of the player");

        placeholder.register("user_has_discord_linked", (player) -> iStorage.getUser(player.getUniqueId()).isDiscordLinked() ? "true" : "false", "Returns true if the player has a discord linked");
    }
}
