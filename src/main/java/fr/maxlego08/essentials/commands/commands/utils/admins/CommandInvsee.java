package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.nms.PlayerUtil;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.menu.zcore.utils.nms.NmsVersion;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Constructor;

public class CommandInvsee extends VCommand {
    public CommandInvsee(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_INVSEE);
        this.setDescription(Message.DESCRIPTION_INVSEE);
        this.addRequireOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        this.user.setOption(Option.INVSEE, true);

        if (offlinePlayer.isOnline()) {

            this.player.openInventory(player.getInventory());

        } else {

            if (!hasPermission(sender, Permission.ESSENTIALS_INVSEE_OFFLINE)) return CommandResultType.SYNTAX_ERROR;

            String version = NmsVersion.getCurrentVersion().name().replace("V_", "v");
            String className = String.format("fr.maxlego08.essentials.nms.%s.PlayerUtils", version);

            try {

                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getConstructor(EssentialsPlugin.class);
                PlayerUtil playerUtil = (PlayerUtil) constructor.newInstance(this.plugin);
                if (!playerUtil.openPlayerInventory(player, offlinePlayer)) {
                    this.user.setOption(Option.INVSEE, false);
                    return CommandResultType.SYNTAX_ERROR;
                }

            } catch (Exception exception) {
                this.player.closeInventory();
                this.user.setOption(Option.INVSEE, false);
                this.plugin.getLogger().severe("Cannot create a new instance for the class " + className);
                this.plugin.getLogger().severe(exception.getMessage());
            }
        }

        return CommandResultType.SUCCESS;
    }
}
