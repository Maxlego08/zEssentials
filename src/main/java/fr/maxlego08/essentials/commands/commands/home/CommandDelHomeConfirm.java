package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.home.HomeManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandDelHomeConfirm extends VCommand {

    public CommandDelHomeConfirm(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_DEL_HOME_CONFIRM);
        this.setDescription(Message.DESCRIPTION_DEL_HOME_CONFIRM);
        this.addRequireArg("name", (sender, args) -> {
            if (sender instanceof Player player) {
                User user = plugin.getUser(player.getUniqueId());
                return user.getHomes().stream().map(Home::getName).toList();
            }
            return new ArrayList<>();
        });
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String homeName = this.argAsString(0);
        HomeManager homeManager = plugin.getHomeManager();

        // For /delhome Maxlego08:<home name>
        if (homeName.contains(":") && hasPermission(sender, Permission.ESSENTIALS_SET_HOME_OTHER)) {
            String[] values = homeName.split(":", 2);
            String username = values[0];
            String home = values[1];
            homeManager.deleteHome(this.sender, username, home);
            return CommandResultType.DEFAULT;
        }

        homeManager.deleteHome(this.player, this.user, homeName);

        return CommandResultType.SUCCESS;
    }
}
