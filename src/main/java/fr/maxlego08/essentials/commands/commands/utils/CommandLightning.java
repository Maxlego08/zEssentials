package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public final class CommandLightning extends VCommand {

    public CommandLightning(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_LIGHTING);
        this.setDescription(Message.DESCRIPTION_LIGHTING);
        this.addOptionalArg("player", (a, b) -> new ArrayList<>(plugin.getEssentialsServer().getVisiblePlayerNames(this.sender)) {{
            add("*");
        }});
        this.addOptionalArg("visual-flag", (a, b) -> List.of("--only-visual"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        String arg = this.argAsString(0);

        if ("*".equals(arg)) {
            return strikeAllPlayers(plugin);
        }

        return strikeSinglePlayer();
    }

    private CommandResultType strikeAllPlayers(EssentialsPlugin plugin) {
        if (!hasPermission(sender, Permission.ESSENTIALS_LIGHTING_ALL)) {
            return CommandResultType.NO_PERMISSION;
        }

        boolean visual = this.argAsString(1, "").equals("--only-visual");

        for (Player target : plugin.getServer().getOnlinePlayers()) {
            if (!target.isValid()) continue;
            strike(target.getLocation(), visual);
            message(target, Message.COMMAND_LIGHTING_RECEIVER);
        }

        message(sender, Message.COMMAND_LIGHTING_ALL);
        return CommandResultType.SUCCESS;
    }

    private CommandResultType strikeSinglePlayer() {
        String arg = this.argAsString(0);
        Player player = this.player;
        if (arg != null && hasPermission(sender, Permission.ESSENTIALS_LIGHTING_OTHER)) {
            player = Bukkit.getPlayer(arg);
            if (player == null) return CommandResultType.SYNTAX_ERROR;
        }

        if (!player.isValid()) {
            message(sender, Message.COMMAND_LIGHTING_ERROR);
            return CommandResultType.DEFAULT;
        }

        Location location;

        if (arg == null) {
            final Block targetBlockExact = player.getTargetBlock(null, 100);
            location = targetBlockExact.getLocation().add(.5d, 0, .5d);

            message(sender, Message.COMMAND_LIGHTING);
        } else if (player == this.player){
            location = player.getLocation();

            message(player, Message.COMMAND_LIGHTING_RECEIVER);
        } else {
            location = player.getLocation();

            message(sender, Message.COMMAND_LIGHTING_SENDER, "%player%", player.getName());
            message(player, Message.COMMAND_LIGHTING_RECEIVER);
        }

        strike(location, this.argAsString(1, "").equals("--only-visual"));

        return CommandResultType.SUCCESS;
    }

    private void strike(Location location, boolean visual) {
        if (visual) {
            location.getWorld().strikeLightning(location);
        } else {
            location.getWorld().strikeLightningEffect(location);
        }
    }
}
