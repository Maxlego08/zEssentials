package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandSetHomeConfirm extends VCommand {

    public CommandSetHomeConfirm(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_SET_HOME_CONFIRM);
        this.setDescription(Message.DESCRIPTION_SET_HOME_CONFIRM);
        this.addRequireArg("name");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String homeName = this.argAsString(0);
        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);

        if (homeModule.getDisableWorlds().contains(player.getWorld().getName())) {
            message(sender, Message.COMMAND_SET_HOME_WORLD);
            return CommandResultType.DEFAULT;
        }

        Message nameResult = homeModule.isValidHomeName(homeName);
        if (nameResult != null) {
            message(sender, nameResult, "%name%", homeName);
            return CommandResultType.DEFAULT;
        }

        int maxHome = plugin.getMaxHome(player);
        int current = user.countHomes();

        if (current >= maxHome && !this.user.isHomeName(homeName)) {
            message(sender, Message.COMMAND_SET_HOME_MAX, "%name%", homeName, "%max%", maxHome, "%current%", current);
            return CommandResultType.DEFAULT;
        }

        user.setHome(homeName, player.getLocation(), true);
        message(sender, Message.COMMAND_SET_HOME_CREATE, "%name%", homeName, "%max%", maxHome, "%current%", user.countHomes());
        return CommandResultType.SUCCESS;
    }
}
