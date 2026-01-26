package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.zutils.utils.AttributeUtils;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class CommandHeal extends VCommand {
    public CommandHeal(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_HEAL);
        this.setDescription(Message.DESCRIPTION_HEAL);
        this.addOptionalArg("player", getVisiblePlayerNames());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) return CommandResultType.SYNTAX_ERROR;

        if (player != this.player && !hasPermission(sender, Permission.ESSENTIALS_HEAL_OTHER)) {
            player = this.player;
        }

        if (!player.isValid()) {
            message(sender, Message.COMMAND_HEAL_ERROR);
            return CommandResultType.DEFAULT;
        }

        player.setHealth(player.getAttribute(AttributeUtils.getAttribute("max_health")).getBaseValue());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        Player finalPlayer = player;
        var user = plugin.getUser(player.getUniqueId());
        player.getActivePotionEffects().forEach(potionEffect -> {
            if (potionEffect.getType() == PotionEffectType.NIGHT_VISION && user.getOption(Option.NIGHT_VISION)) return;
            finalPlayer.removePotionEffect(potionEffect.getType());
        });

        if (player == sender) {

            message(sender, Message.COMMAND_HEAL_RECEIVER);
        } else {

            message(sender, Message.COMMAND_HEAL_SENDER, "%player%", player.getName());
            message(player, Message.COMMAND_HEAL_RECEIVER);
        }

        return CommandResultType.SUCCESS;
    }
}
