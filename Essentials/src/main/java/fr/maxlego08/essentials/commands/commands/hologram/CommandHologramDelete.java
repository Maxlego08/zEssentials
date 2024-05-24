package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.hologram.HologramModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandHologramDelete extends VCommand {

    public CommandHologramDelete(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HologramModule.class);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_DELETE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_DELETE);
        this.addSubCommand("delete");
        this.addRequireArg("name", plugin.getHologramManager().getHologramCompletion());
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String name = this.argAsString(1);

        plugin.getHologramManager().delete(player, name);

        return CommandResultType.SUCCESS;
    }
}
