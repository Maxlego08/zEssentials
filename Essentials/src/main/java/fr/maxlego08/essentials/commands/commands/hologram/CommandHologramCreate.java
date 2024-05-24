package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.hologram.HologramModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;

public class CommandHologramCreate extends VCommand {

    public CommandHologramCreate(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HologramModule.class);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_CREATE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_CREATE);
        this.addSubCommand("create");
        this.addRequireArg("type", (a, b) -> Arrays.stream(HologramType.values()).map(HologramType::name).toList());
        this.addRequireArg("name");
        this.setExtendedArgs(true);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        HologramType hologramType = HologramType.valueOf(this.argAsString(0));
        String name = this.argAsString(1);

        plugin.getHologramManager().create(player, hologramType, name);

        return CommandResultType.SUCCESS;
    }
}
