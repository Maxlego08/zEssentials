package fr.maxlego08.essentials.commands.commands.teleport.random;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

public class CommandTeleportRandomOther extends VCommand {

    public CommandTeleportRandomOther(EssentialsPlugin plugin) {
        super(plugin);
        super.setModule(TeleportationModule.class);
        super.setPermission(Permission.ESSENTIALS_TP_RANDOM_OTHER);
        super.setDescription(Message.DESCRIPTION_TP_RANDOM_OTHER);
        super.addSubCommand("other");
        super.addRequirePlayerNameArg();
        super.addRequireArg("world", (a, b) -> Bukkit.getWorlds().stream().map(WorldInfo::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player target = this.argAsPlayer(0);
        World world = this.argAsWorld(1);

        if (target == null || world == null) return CommandResultType.SYNTAX_ERROR;

        TeleportationModule module = plugin.getModuleManager().getModule(TeleportationModule.class);
        module.randomTeleport(target, world);

        message(sender, Message.COMMAND_RANDOM_TP_OTHER, target, "%world%", world.getName());
        return CommandResultType.SUCCESS;
    }
}
