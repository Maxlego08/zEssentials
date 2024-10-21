package fr.maxlego08.essentials.commands.commands.fly;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandFlyGet extends VCommand {
    public CommandFlyGet(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("get");
        this.setPermission(Permission.ESSENTIALS_FLY_GET);
        this.setDescription(Message.DESCRIPTION_FLY_GET);
        this.addRequireOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String userName = this.argAsString(0);

        fetchUniqueId(userName, uniqueId -> {
            var storage = plugin.getStorageManager().getStorage();
            var user = plugin.getUser(uniqueId);
            long flySeconds = user == null ? storage.getFlySeconds(uniqueId) : user.getFlySeconds();

            message(sender, Message.COMMAND_FLY_GET, "%player%", userName, "%time%", TimerBuilder.getStringTime(flySeconds * 1000));
        });

        return CommandResultType.SUCCESS;
    }
}
