package fr.maxlego08.essentials.commands.commands;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandEssentials extends VCommand {

    public CommandEssentials(EssentialsPlugin plugin) {
        super(plugin);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        sender.sendMessage(Component.text(getMessage("zEssentials, version %version%", "%version%", plugin.getDescription().getVersion()), NamedTextColor.YELLOW));

        return CommandResultType.SUCCESS;
    }
}
