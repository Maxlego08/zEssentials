package fr.maxlego08.essentials.commands.commands.kits;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.kit.KitModule;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Optional;

public class CommandKitCreate extends VCommand {
    public CommandKitCreate(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SanctionModule.class);
        this.setPermission(Permission.ESSENTIALS_KIT_CREATE);
        this.setDescription(Message.DESCRIPTION_KIT_CREATE);
        this.addRequireArg("name");
        this.addRequireArg("cooldown");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        KitModule kitModule = plugin.getModuleManager().getModule(KitModule.class);
        String kitName = this.argAsString(0);
        int cooldown = this.argAsInteger(1);

        Optional<Kit> optional = kitModule.getKit(kitName);
        if (optional.isPresent()) {
            message(sender, Message.COMMAND_KIT_ALREADY_EXIT, "%kit%", kitName);
            return CommandResultType.DEFAULT;
        }

        kitModule.createKit(this.player, kitName, Math.max(1, cooldown));
        return CommandResultType.SUCCESS;
    }
}
