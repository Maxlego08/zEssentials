package fr.maxlego08.essentials.commands.commands.gamemode;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandGameMode extends GameModeCommand {

    public CommandGameMode(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_GAMEMODE);
        this.setDescription(Message.DESCRIPTION_GAMEMODE);
        this.addRequireArg("gamemode", (a, b) -> Arrays.stream(GameMode.values()).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String value = this.argAsString(0);
        Player player = this.argAsPlayer(1, this.player);

        if (player == null) {
            message(sender, Message.COMMAND_GAMEMODE_INVALID);
            return CommandResultType.DEFAULT;
        }

        Optional<GameMode> optional = Arrays.stream(GameMode.values()).filter(e -> value.equals(String.valueOf(e.getValue())) || e.name().equalsIgnoreCase(value)).findFirst();
        if (optional.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        GameMode gameMode = optional.get();
        return changeGameMode(this.sender, gameMode, player);
    }
}
