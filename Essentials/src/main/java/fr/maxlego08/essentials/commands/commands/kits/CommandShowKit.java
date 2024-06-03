package fr.maxlego08.essentials.commands.commands.kits;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.kit.KitModule;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Optional;

public class CommandShowKit extends VCommand {
    public CommandShowKit(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(KitModule.class);
        this.setPermission(Permission.ESSENTIALS_KIT_SHOW);
        this.setDescription(Message.DESCRIPTION_KIT_SHOW);
        this.addRequireArg("kit", (sender, b) -> plugin.getModuleManager().getModule(KitModule.class).getKits(sender).stream().map(Kit::getName).toList());
        this.onlyPlayers();
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

        user.openKitPreview(optional.get());

        return CommandResultType.DEFAULT;
    }
}
