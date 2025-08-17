package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.util.Optional;

public class UserKitPlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        placeholder.register("user_has_kit_", (player, kitName) -> {
            Optional<Kit> optional = plugin.getKit(kitName);
            return optional.map(kit -> String.valueOf(kit.hasPermission(player))).orElseGet(() -> "Kit " + kitName + " was not found");
        }, "Returns true or false if the player has the kit", "kit name");

        placeholder.register("user_kit_is_available_", (player, kitName) -> {
            Optional<Kit> optional = plugin.getKit(kitName);
            User user = plugin.getUser(player.getUniqueId());
            return optional.map(kit -> String.valueOf(user.isKitCooldown(kit))).orElseGet(() -> "Kit " + kitName + " was not found");
        }, "Returns true if the player can get the kit", "kit name");

        placeholder.register("user_kit_time_until_available_", (player, kitName) -> {
            Optional<Kit> optional = plugin.getKit(kitName);
            User user = plugin.getUser(player.getUniqueId());
            if (user == null) return "false";
            if (optional.isEmpty()) return "Kit " + kitName + " was not found";

            Kit kit = optional.get();
            if (!user.isKitCooldown(kit)) return TimerBuilder.getStringTime(0);
            long kitCooldown = user.getKitCooldown(optional.get());
            return TimerBuilder.getStringTime(kitCooldown - System.currentTimeMillis());

        }, "Returns the time before the player can use the kit again", "kit name");

    }
}
