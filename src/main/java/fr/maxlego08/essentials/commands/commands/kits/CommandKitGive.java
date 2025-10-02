package fr.maxlego08.essentials.commands.commands.kits;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.kit.KitModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Optional;

public class CommandKitGive extends VCommand {

    public CommandKitGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(KitModule.class);
        this.setPermission(Permission.ESSENTIALS_KIT_GIVE);
        this.setDescription(Message.DESCRIPTION_KIT_GIVE);
        this.addRequirePlayerNameArg();
        this.addRequireArg("kit", (sender, b) -> plugin.getModuleManager().getModule(KitModule.class).getKits(sender).stream().map(Kit::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        KitModule kitModule = plugin.getModuleManager().getModule(KitModule.class);
        var player = this.argAsPlayer(0);
        String kitName = this.argAsString(1);

        if (player == null) return CommandResultType.SYNTAX_ERROR;

        Optional<Kit> optional = kitModule.getKit(kitName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_KIT_NOT_FOUND, "%kit%", kitName);
            return CommandResultType.DEFAULT;
        }

        var kit = optional.get();
        kit.give(player);

        message(sender, Message.COMMAND_KIT_GIVE, "%kit%", kitName, "%player%", player.getName());

        return CommandResultType.SUCCESS;
    }
}
