package fr.maxlego08.essentials.commands.commands.teleport.random;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandTeleportRandom extends VCommand {

    public CommandTeleportRandom(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TP_RANDOM);
        this.setDescription(Message.DESCRIPTION_TP_RANDOM);
        this.onlyPlayers();
        this.addSubCommand(new CommandTeleportRandomOther(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        TeleportationModule module = plugin.getModuleManager().getModule(TeleportationModule.class);
        module.randomTeleport(player, player.getWorld());

        return CommandResultType.SUCCESS;
    }
}
