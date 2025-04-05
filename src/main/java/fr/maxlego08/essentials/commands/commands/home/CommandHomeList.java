package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandHomeList extends VCommand {

    public CommandHomeList(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_LIST);
        this.setDescription(Message.DESCRIPTION_HOME);
        this.onlyPlayers();
        this.addRequireOfflinePlayerNameArg();
        this.addOptionalArg("name", (sender, args) -> {

            String name = args[0];
            Player player = Bukkit.getPlayerExact(name);
            if (player == null) return new ArrayList<>();

            var user = plugin.getUser(player.getUniqueId());
            if (user == null) return new ArrayList<>();

            return user.getHomes().stream().map(Home::getName).toList();
        });
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);
        homeModule.teleport(this.user, this.argAsString(0), this.argAsString(1, null));

        return CommandResultType.SUCCESS;
    }
}
