package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.essentials.zutils.utils.cube.CubeDisplay;
import org.bukkit.Color;

public class CommandEssentialsTest extends VCommand {

    public CommandEssentialsTest(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("test");
        this.setPermission(Permission.ESSENTIALS_RELOAD);
        this.setDescription(Message.DESCRIPTION_RELOAD);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        CubeDisplay cube = new CubeDisplay(player.getLocation(), 3, 3, 3, Color.fromRGB(255, 0, 0));
        cube.spawn();

        return CommandResultType.SUCCESS;
    }
}
