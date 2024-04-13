package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Optional;

public class CommandHome extends VCommand {

    public CommandHome(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME);
        this.setDescription(Message.DESCRIPTION_HOME);
        this.addOptionalArg("name");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String homeName = this.argAsString(0, null);

        if (homeName == null) {
            sendHomes(plugin);
            return CommandResultType.SUCCESS;
        }


        // For /sethome Maxlego08:<home name>
        if (homeName.contains(":") && hasPermission(sender, Permission.ESSENTIALS_HOME_OTHER)) {
            // ToDo
        }

        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);

        Optional<Home> optional = this.user.getHome(homeName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_HOME_DOESNT_EXIST, "%name%", homeName);
            return CommandResultType.DEFAULT;
        }

        Home home = optional.get();
        this.user.teleport(home.getLocation(), Message.TELEPORT_MESSAGE_HOME, Message.TELEPORT_SUCCESS_HOME, "%name%", home.getName());

        return CommandResultType.SUCCESS;
    }

    private void sendHomes(EssentialsPlugin plugin) {

    }
}
