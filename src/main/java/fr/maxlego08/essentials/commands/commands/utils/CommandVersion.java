package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandVersion extends VCommand {
    public CommandVersion(EssentialsPlugin plugin) {
        super(plugin);
        this.setDescription(Message.DESCRIPTION_VERSION);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {


        message(this.sender, "<c:#55FF55>Version du plugin<c:#AAAAAA>: <c:#00AA00>" + plugin.getPluginMeta().getVersion());
        message(this.sender, "<c:#55FF55>Auteur<c:#AAAAAA>: <c:#00AA00>Maxlego08");
        message(this.sender, "<c:#55FF55>Discord<c:#AAAAAA>: <c:#00AA00>http://discord.groupez.dev/");
        message(this.sender, "<c:#55FF55>Buy it for <c:#FF55FF>20€<c:#AAAAAA>: <c:#00AA00>https://www.spigotmc.org/resources/116293/");
        message(this.sender, "<c:#55FF55>Sponsor<c:#AAAAAA>: <c:#FF5555>https://minecraft-inventory-builder.com/");

        return CommandResultType.SUCCESS;
    }
}
