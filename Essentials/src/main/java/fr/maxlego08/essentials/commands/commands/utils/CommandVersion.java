package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandVersion extends VCommand {
    public CommandVersion(EssentialsPlugin plugin) {
        super(plugin);
        this.setDescription(Message.DESCRIPTION_TRASH);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {


        message(this.sender, "§aVersion du plugin§7: §2" + plugin.getDescription().getVersion());
        message(this.sender, "§aAuteur§7: §2Maxlego08");
        message(this.sender, "§aDiscord§7: §2http://discord.groupez.dev/");
        message(this.sender, "§aBuy it for §d20€§7: §2https://www.spigotmc.org/resources/116293/");
        message(this.sender, "§aSponsor§7: §chttps://minecraft-inventory-builder.com/");

        return CommandResultType.SUCCESS;
    }
}
