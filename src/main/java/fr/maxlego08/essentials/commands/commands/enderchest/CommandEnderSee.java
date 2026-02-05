package fr.maxlego08.essentials.commands.commands.enderchest;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.nms.PlayerUtil;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.menu.common.utils.nms.NmsVersion;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Constructor;

public class CommandEnderSee extends VCommand {

    public CommandEnderSee(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ENDERSEE);
        this.setDescription(Message.DESCRIPTION_ENDERSEE);
        this.addRequireOfflinePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        if (offlinePlayer.isOnline()) {

            var targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return CommandResultType.SYNTAX_ERROR;
            this.player.openInventory(targetPlayer.getEnderChest());

        } else {

            if (!hasPermission(sender, Permission.ESSENTIALS_ENDERSEE_OFFLINE)) return CommandResultType.NO_PERMISSION;

            String version = NmsVersion.getCurrentVersion().name().replace("V_", "v");
            String className = String.format("fr.maxlego08.essentials.nms.%s.PlayerUtils", version);

            try {

                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getConstructor(EssentialsPlugin.class);
                PlayerUtil playerUtil = (PlayerUtil) constructor.newInstance(this.plugin);
                if (!playerUtil.openEnderChest(player, offlinePlayer)) {
                    message(sender, Message.COMMAND_ENDERSEE_ERROR, "%player%", offlinePlayer.getName());
                    return CommandResultType.DEFAULT;
                }

            } catch (Exception exception) {
                this.plugin.getLogger().severe("Cannot create a new instance for the class " + className);
                this.plugin.getLogger().severe(exception.getMessage());
                message(sender, Message.COMMAND_ENDERSEE_ERROR, "%player%", offlinePlayer.getName());
                return CommandResultType.DEFAULT;
            }

        }
        return CommandResultType.SUCCESS;
    }
}
