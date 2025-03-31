package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.module.modules.hologram.HologramModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandHologramList extends VCommand {

    public CommandHologramList(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HologramModule.class);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_LIST);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_LIST);
        this.addSubCommand("list", "l", "?");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        HologramManager manager = plugin.getHologramManager();

        if (manager.getHolograms().isEmpty()) {
            message(sender, Message.HOLOGRAM_LIST_EMPTY);
            return CommandResultType.DEFAULT;
        }

        for (Hologram hologram : manager.getHolograms()) {
            SafeLocation location = hologram.getLocation();
            message(sender, Message.HOLOGRAM_LIST, "%name%", hologram.getName(), "%x%", location.getBlockX(), "%y%", location.getBlockY(), "%z%", location.getBlockZ(), "%world%", location.getWorld());
        }

        return CommandResultType.SUCCESS;
    }
}
