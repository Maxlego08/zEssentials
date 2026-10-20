package fr.maxlego08.essentials.commands.commands.utils.blocks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.inventory.MenuType;

public class CommandGrindStone extends VCommand {
    public CommandGrindStone(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_GRINDSTONE);
        this.setDescription(Message.DESCRIPTION_GRINDSTONE);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.player.openInventory(MenuType.GRINDSTONE.builder().location(this.player.getLocation()).checkReachable(false).build(this.player));

        return CommandResultType.SUCCESS;
    }
}
