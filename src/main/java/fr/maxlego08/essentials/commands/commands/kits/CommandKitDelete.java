package fr.maxlego08.essentials.commands.commands.kits;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.kit.KitModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Optional;

public class CommandKitDelete extends VCommand {
    public CommandKitDelete(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(KitModule.class);
        this.setPermission(Permission.ESSENTIALS_KIT_DELETE);
        this.setDescription(Message.DESCRIPTION_KIT_DELETE);
        this.addOptionalArg("kit", (sender, b) -> plugin.getModuleManager().getModule(KitModule.class).getKits(sender).stream().map(Kit::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        KitModule kitModule = plugin.getModuleManager().getModule(KitModule.class);
        String kitName = this.argAsString(0);

        Optional<Kit> optional = kitModule.getKit(kitName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_KIT_NOT_FOUND, "%kit%", kitName);
            return CommandResultType.DEFAULT;
        }

        Kit kit = optional.get();
        kitModule.deleteKit(this.player, kit);
        return CommandResultType.SUCCESS;
    }
}
