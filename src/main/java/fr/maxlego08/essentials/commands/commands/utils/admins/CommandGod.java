package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.AttributeUtils;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CommandGod extends VCommand {
    // Constants at class level
    private static final double DEFAULT_MAX_HEALTH = 20.0;
    private static final int DEFAULT_MAX_FOOD_LEVEL = 20;

    public CommandGod(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_GOD);
        this.setDescription(Message.DESCRIPTION_GOD);
        this.addOptionalArg("player", getOnlinePlayers());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player) {
            // Self god mode - uses the existing permission
            toggleGodMode(player, this.user, this.sender);
        } else {
            // God mode for others - check for additional permission
            if (!hasPermission(this.sender, Permission.ESSENTIALS_GOD_OTHER)) {
                message(this.sender, Message.COMMAND_NO_PERMISSION);
                return CommandResultType.NO_PERMISSION;
            }

            User otherUser = getUser(player);
            toggleGodMode(player, otherUser, this.sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void toggleGodMode(Player target, User user, CommandSender sender) {
        boolean wasGodEnabled = user.getOption(Option.GOD);
        user.setOption(Option.GOD, !wasGodEnabled);
        boolean isGodEnabled = user.getOption(Option.GOD);

        if (isGodEnabled) {
            try {
                setPlayerHealthAndFood(target);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error setting god mode health/food for " + target.getName(), e);
                // Revert god mode state to maintain consistency
                user.setOption(Option.GOD, wasGodEnabled);
                isGodEnabled = wasGodEnabled;
            }
        }

        // Send messages
        sendGodModeMessages(target, sender, isGodEnabled);
    }

    private void sendGodModeMessages(Player target, CommandSender sender, boolean isGodEnabled) {
        Message messageKey = isGodEnabled ? Message.COMMAND_GOD_ENABLE : Message.COMMAND_GOD_DISABLE;

        // Message for the sender
        boolean isSelf = sender.equals(target);
        String senderMessage = isSelf ? Message.YOU.getMessageAsString() : target.getName();
        message(sender, messageKey, "%player%", senderMessage);

        // Message for the target (if different from sender)
        if (!isSelf) {
            message(target, messageKey, "%player%", Message.YOU.getMessageAsString());
        }
    }

    private void setPlayerHealthAndFood(Player player) {
        // Set health to max health value or fallback to default
        Attribute maxHealthAttribute = AttributeUtils.getAttribute("max_health");
        double maxHealth = DEFAULT_MAX_HEALTH; // Use constant as fallback

        if (maxHealthAttribute != null) {
            AttributeInstance attributeInstance = player.getAttribute(maxHealthAttribute);
            if (attributeInstance != null) {
                maxHealth = attributeInstance.getBaseValue();
            }
        }

        player.setHealth(maxHealth);
        player.setFoodLevel(DEFAULT_MAX_FOOD_LEVEL);
    }
}
