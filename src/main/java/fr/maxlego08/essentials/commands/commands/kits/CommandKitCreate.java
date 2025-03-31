package fr.maxlego08.essentials.commands.commands.kits;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.kit.KitModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.time.Duration;
import java.util.Optional;

public class CommandKitCreate extends VCommand {

    public CommandKitCreate(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(KitModule.class);
        this.setPermission(Permission.ESSENTIALS_KIT_CREATE);
        this.setDescription(Message.DESCRIPTION_KIT_CREATE);
        this.addRequireArg("name", (a, b) -> plugin.getModuleManager().getModule(KitModule.class).getKitNames());
        this.addRequireArg("cooldown", (a, b) -> this.cooldowns);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        KitModule kitModule = plugin.getModuleManager().getModule(KitModule.class);
        String kitName = this.argAsString(0);
        Duration duration = this.argAsDuration(1);

        Optional<Kit> optional = kitModule.getKit(kitName);
        if (optional.isPresent()) {
            message(sender, Message.COMMAND_KIT_ALREADY_EXISTS, "%kit%", kitName);
            return CommandResultType.DEFAULT;
        }

        kitModule.createKit(this.player, kitName, Math.max(0L, duration.toSeconds()));
        return CommandResultType.SUCCESS;
    }
}
