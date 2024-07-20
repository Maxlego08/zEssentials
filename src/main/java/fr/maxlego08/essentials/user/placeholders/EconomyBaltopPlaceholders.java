package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.economy.UserBaltop;
import fr.maxlego08.essentials.api.functionnals.ReturnBiConsumer;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

public class EconomyBaltopPlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {
        EconomyManager economyManager = plugin.getEconomyManager();

        registerEconomyBaltopPlaceholder(placeholder, economyManager, "economy_baltop_name_", (economy, userBaltop) -> userBaltop.getName(), economyManager.getBaltopPlaceholderUserEmpty(), "Returns the player's name for the given economy and position");
        registerEconomyBaltopPlaceholder(placeholder, economyManager, "economy_baltop_uuid_", (economy, userBaltop) -> userBaltop.getUniqueId().toString(), economyManager.getBaltopPlaceholderUserEmpty(), "Returns the player's uuid for the given economy and position");

        placeholder.register("economy_baltop_amount_", registerAmount(false, economyManager), "Returns the player's economy amount for the given economy and position", "economy name", "position");
        placeholder.register("economy_baltop_formatted_amount_", registerAmount(true, economyManager), "Returns the player's economy formatted amount for the given economy and position", "economy name", "position");
    }

    private ReturnBiConsumer<Player, String, String> registerAmount(boolean isFormatted, EconomyManager economyManager) {
        return (player, args) -> {

            try {
                String[] strings = args.split("_");
                String economyName = strings[0];
                int position = Integer.parseInt(strings[1]) - 1;

                Optional<Economy> optionalEconomy = economyManager.getEconomy(economyName);
                if (optionalEconomy.isEmpty()) return "Economy " + economyName + " was not found";
                Economy economy = optionalEconomy.get();

                Optional<UserBaltop> optional = economyManager.getPosition(economyName, position);
                if (optional.isPresent()) {
                    UserBaltop userBaltop = optional.get();
                    return isFormatted ? economyManager.format(economy, userBaltop.getAmount()) : userBaltop.getAmount().toString();
                }
                return isFormatted ? economyManager.format(economy, 0) : "0";

            } catch (Exception ignored) {
            }
            return economyManager.getBaltopPlaceholderUserEmpty();
        };
    }

    private void registerEconomyBaltopPlaceholder(Placeholder placeholder, EconomyManager economyManager, String placeholderPrefix, PlaceholderValueProvider valueProvider, String defaultValue, String description) {
        placeholder.register(placeholderPrefix, (player, args) -> {

            try {
                String[] strings = args.split("_");
                String economyName = strings[0];
                int position = Integer.parseInt(strings[1]) - 1;

                Optional<Economy> optionalEconomy = economyManager.getEconomy(economyName);
                if (optionalEconomy.isEmpty()) return "Economy " + economyName + " was not found";

                Optional<UserBaltop> optional = economyManager.getPosition(economyName, position);
                if (optional.isPresent()) {
                    return valueProvider.getValue(optionalEconomy.get(), optional.get());
                }
            } catch (Exception ignored) {
            }
            return economyManager.getBaltopPlaceholderUserEmpty();
        }, description, "economy name", "position");
    }

    @FunctionalInterface
    private interface PlaceholderValueProvider {
        String getValue(Economy economy, UserBaltop userBaltop);
    }
}
