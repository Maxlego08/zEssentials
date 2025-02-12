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
        this.addRequireArg("width");
        this.addRequireArg("height");
        this.addRequireArg("depth");
        this.addRequireArg("alpha");
        this.addRequireArg("r");
        this.addRequireArg("g");
        this.addRequireArg("b");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        double width = this.argAsDouble(0);
        double height = this.argAsDouble(1);
        double depth = this.argAsDouble(2);
        int a = this.argAsInteger(3, 255);
        int r = this.argAsInteger(4, 45);
        int g = this.argAsInteger(5, 45);
        int b = this.argAsInteger(6, 45);

        CubeDisplay cube = new CubeDisplay(player.getLocation(), width, height, depth, Color.fromARGB(a, r, g, b));
        cube.spawn();

        return CommandResultType.SUCCESS;
    }
}
