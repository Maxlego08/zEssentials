package fr.maxlego08.essentials.commands.commands.gamemode;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CommandGameModeCreative extends GameModeCommand {

    public CommandGameModeCreative(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_GAMEMODE_CREATIVE);
        this.setDescription(Message.DESCRIPTION_GAMEMODE_CREATIVE);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            message(sender, Message.COMMAND_GAMEMODE_INVALID);
            return CommandResultType.DEFAULT;
        }

       return changeGameMode(this.sender, GameMode.CREATIVE, player);
    }
}
