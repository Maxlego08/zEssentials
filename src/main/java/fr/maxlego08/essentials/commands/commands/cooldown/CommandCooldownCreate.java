package fr.maxlego08.essentials.commands.commands.cooldown;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;

import java.time.Duration;

public class CommandCooldownCreate extends VCommand {
    public CommandCooldownCreate(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_COOLDOWN_CREATE);
        this.setDescription(Message.DESCRIPTION_COOLDOWN_CREATE);
        this.addSubCommand("create");
        this.addRequireArg("player");
        this.addRequireArg("key");
        this.addRequireArg("duration", (a, b) -> this.cooldowns);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        CommandSender sender = this.sender;
        String userName = this.argAsString(0);
        String cooldownName = this.argAsString(1);
        Duration duration = this.argAsDuration(2);

        fetchUniqueId(userName, uniqueId -> {

            IStorage iStorage = plugin.getStorageManager().getStorage();
            long expiredAt = System.currentTimeMillis() + duration.toMillis();
            iStorage.updateCooldown(uniqueId, cooldownName, expiredAt);

            plugin.getEssentialsServer().updateCooldown(uniqueId, cooldownName, expiredAt);
            message(sender, Message.COMMAND_COOLDOWN_CREATE, "%key%", cooldownName, "%player%", userName, "%duration%", TimerBuilder.getStringTime(duration.toMillis()));
        });

        return CommandResultType.SUCCESS;
    }
}
