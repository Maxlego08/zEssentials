package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandWorldEditGive extends VCommand {

    public CommandWorldEditGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_GIVE);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_GIVE);
        this.addSubCommand("give");
        this.addRequirePlayerNameArg();
        this.addRequireArg("item", (a, b) -> plugin.getWorldeditManager().getWorldeditItems());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String itemName = this.argAsString(1);

        plugin.getWorldeditManager().give(this.sender, player, itemName);

        return CommandResultType.SUCCESS;
    }
}
