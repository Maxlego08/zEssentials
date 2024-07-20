package fr.maxlego08.essentials.commands.commands.gamemode;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GameModeCommand extends VCommand {
    public GameModeCommand(EssentialsPlugin plugin) {
        super(plugin);
    }

    protected CommandResultType changeGameMode(CommandSender sender, GameMode gameMode, Player player) {
        if (player == this.player) {

            player.setGameMode(gameMode);
            message(sender, Message.COMMAND_GAMEMODE, "%gamemode%", name(gameMode.name()), "%player%", Message.YOU.getMessageAsString());

        } else {

            if (!hasPermission(sender, Permission.ESSENTIALS_GAMEMODE_OTHER)) return CommandResultType.NO_PERMISSION;

            player.setGameMode(gameMode);
            message(sender, Message.COMMAND_GAMEMODE, "%gamemode%", name(gameMode.name()), "%player%", player.getName());
        }
        return CommandResultType.SUCCESS;
    }
}
