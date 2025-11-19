package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.economy.EconomyModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CommandEconomyResetAll extends VCommand {

    private static final long CONFIRMATION_DURATION = TimeUnit.SECONDS.toMillis(30);
    // Thread-safe confirmation map
    private static final Map<String, Confirmation> CONFIRMATIONS = new java.util.concurrent.ConcurrentHashMap<>();

    public CommandEconomyResetAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_ECO_RESET_ALL);
        this.setDescription(Message.DESCRIPTION_ECO_RESET_ALL);
        this.addSubCommand("reset-all");
        this.addRequireArg("economy", (sender, args) -> plugin.getEconomyManager().getEconomies().stream().map(Economy::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);
        EconomyManager economyManager = plugin.getEconomyManager();
        Optional<Economy> optional = economyManager.getEconomy(economyName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }

        Economy economy = optional.get();
        long now = System.currentTimeMillis();
        String key = getConfirmationKey(this.sender);
        Confirmation confirmation = CONFIRMATIONS.get(key);

        if (confirmation == null || confirmation.isExpired(now) || !confirmation.isSameEconomy(economy.getName())) {
            CONFIRMATIONS.put(key, new Confirmation(economy.getName(), now + CONFIRMATION_DURATION));
            message(sender, Message.COMMAND_ECONOMY_RESET_ALL_CONFIRM, "%economy%", economy.getDisplayName(), "%seconds%", (int) TimeUnit.MILLISECONDS.toSeconds(CONFIRMATION_DURATION));
            return CommandResultType.DEFAULT;
        }

        CONFIRMATIONS.remove(key);

        String reason = this.getMessage(economyManager.getCommandResetReason(), "%sender%", sender.getName());
        economyManager.resetAll(economy, reason);

        message(sender, Message.COMMAND_ECONOMY_RESET_ALL_SUCCESS, "%economy%", economy.getDisplayName());

        return CommandResultType.SUCCESS;
    }

    private String getConfirmationKey(CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getUniqueId().toString();
        }
        return sender.getName();
    }

    private record Confirmation(String economyName, long expiresAt) {

        private boolean isExpired(long now) {
            return now > this.expiresAt;
        }

        private boolean isSameEconomy(String name) {
            return this.economyName.equalsIgnoreCase(name);
        }
    }
}
