package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class CommandNear extends VCommand {
    public CommandNear(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_NEAR);
        this.setDescription(Message.DESCRIPTION_NEAR);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        double distance = plugin.getConfiguration().getNearDistance(this.player);
        List<Player> players = this.player.getWorld().getNearbyPlayers(this.player.getLocation(), distance).stream().filter(player -> {
            User user = plugin.getUser(player.getUniqueId());
            return user == null || !user.getOption(Option.VANISH);
        }).filter(player -> player != this.player).toList();

        if (players.isEmpty()) {
            message(sender, Message.COMMAND_NEAR_EMPTY);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            message(sender, Message.COMMAND_NEAR_PLAYER, "%players%", String.join(",", players.stream().map(player -> getMessage(Message.COMMAND_NEAR_INFO, "%player%", player.getName(), "%distance%", decimalFormat.format(player.getLocation().distance(this.player.getLocation())))).toList()));
        }

        return CommandResultType.SUCCESS;
    }
}
