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

import java.util.ArrayList;

public class CommandHeal extends VCommand {
    public CommandHeal(EssentialsPlugin plugin) {
        super(plugin);
        super.setPermission(Permission.ESSENTIALS_HEAL);
        super.setDescription(Message.DESCRIPTION_HEAL);
        super.addOptionalArg("player", (a, b) -> new ArrayList<>(this.plugin.getEssentialsServer().getVisiblePlayerNames(this.sender)) {{
            add("*");
        }});
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        String arg = this.argAsString(0);

        if ("*".equals(arg)) {
            return healAllPlayers(plugin);
        }

        return healSinglePlayer(plugin);
    }

    private CommandResultType healAllPlayers(EssentialsPlugin plugin) {
        if (!hasPermission(sender, Permission.ESSENTIALS_HEAL_OTHER)) {
            return CommandResultType.NO_PERMISSION;
        }

        for (Player target : plugin.getServer().getOnlinePlayers()) {
            if (!target.isValid()) continue;
            healPlayer(plugin, target);
            message(target, Message.COMMAND_HEAL_RECEIVER);
        }

        message(sender, Message.COMMAND_HEAL_ALL);
        return CommandResultType.SUCCESS;
    }

    private CommandResultType healSinglePlayer(EssentialsPlugin plugin) {
        Player player = this.argAsPlayer(0, this.player);
        if (player == null) return CommandResultType.SYNTAX_ERROR;

        if (player != this.player && !hasPermission(sender, Permission.ESSENTIALS_HEAL_OTHER)) {
            player = this.player;
        }

        if (!player.isValid()) {
            message(sender, Message.COMMAND_HEAL_ERROR);
            return CommandResultType.DEFAULT;
        }

        healPlayer(plugin, player);

        if (player == sender) {
            message(sender, Message.COMMAND_HEAL_RECEIVER);
        } else {
            message(sender, Message.COMMAND_HEAL_SENDER, "%player%", player.getName());
            message(player, Message.COMMAND_HEAL_RECEIVER);
        }

        return CommandResultType.SUCCESS;
    }

    private void healPlayer(EssentialsPlugin plugin, Player player) {
        player.setHealth(
                player.getAttribute(AttributeUtils.getAttribute("max_health")).getBaseValue()
        );
        player.setFoodLevel(20);
        player.setFireTicks(0);

        var user = plugin.getUser(player.getUniqueId());
        player.getActivePotionEffects().forEach(effect -> {
            if (effect.getType() == PotionEffectType.NIGHT_VISION
                    && user.getOption(Option.NIGHT_VISION)) return;
            player.removePotionEffect(effect.getType());
        });
    }
}
