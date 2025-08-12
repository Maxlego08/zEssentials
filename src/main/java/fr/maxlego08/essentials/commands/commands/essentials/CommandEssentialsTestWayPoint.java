package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.waypoint.WayPointIcon;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Location;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class CommandEssentialsTestWayPoint extends VCommand {

    public CommandEssentialsTestWayPoint(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("test-waypoint");
        this.setPermission(Permission.ESSENTIALS_RELOAD);
        this.setDescription(Message.DESCRIPTION_RELOAD);
        this.onlyPlayers();
        this.addRequireArg("method", (a, b) -> List.of("create", "update", "remove"));
        this.addRequireArg("uuid", (a, b) -> List.of(UUID.randomUUID().toString()));
        this.addRequireArg("x");
        this.addRequireArg("y");
        this.addRequireArg("z");
        this.addRequireArg("color");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        var method = this.argAsString(0);
        var uuid = UUID.fromString(this.argAsString(1));
        int x = this.argAsInteger(2);
        int y = this.argAsInteger(3);
        int z = this.argAsInteger(4);
        int color = this.argAsInteger(5);

        var location = new Location(player.getWorld(), x, y, z);
        var helper = plugin.getWayPointHelper();

        if (method.equals("create")) {
            helper.addWayPoint(player, uuid, location, WayPointIcon.of(new Color(color)));
            message(sender, "&fWayPoint created!");
        } else if (method.equals("update")) {
            helper.updateWayPointPosition(player, uuid, location, WayPointIcon.of(new Color(color)));
            message(sender, "&fWayPoint updated!");
        } else if (method.equals("remove")) {
            helper.removeWayPoint(player, uuid);
            message(sender, "&fWayPoint deleted!");
        } else {
            return CommandResultType.SYNTAX_ERROR;
        }

        return CommandResultType.SUCCESS;
    }
}
