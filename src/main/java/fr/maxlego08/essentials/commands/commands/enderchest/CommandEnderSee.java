package fr.maxlego08.essentials.commands.commands.enderchest;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandEnderSee extends VCommand {

    public CommandEnderSee(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ENDERSEE);
        this.setDescription(Message.DESCRIPTION_ENDERSEE);
        this.addRequireOfflinePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player targetPlayer = this.argAsPlayer(0);
        this.player.openInventory(targetPlayer.getEnderChest());
        return CommandResultType.SUCCESS;
    }
}
