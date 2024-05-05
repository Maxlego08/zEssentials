package fr.maxlego08.essentials.commands.commands.cooldown;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommandCooldown extends VCommand {
    public CommandCooldown(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_COOLDOWN);
        this.setDescription(Message.DESCRIPTION_COOLDOWN);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        CommandSender sender = this.sender;
        String userName = this.argAsString(0);

        fetchUniqueId(userName, uniqueId -> {

            IStorage iStorage = plugin.getStorageManager().getStorage();
            List<CooldownDTO> cooldownDTOS = iStorage.getCooldowns(uniqueId).stream().filter(dto -> dto.cooldown_value() > System.currentTimeMillis()).toList();

            if (cooldownDTOS.isEmpty()) {
                message(sender, Message.COMMAND_COOLDOWN_EMPTY, "%player%", userName);
                return;
            }

            SimpleDateFormat simpleDateFormat = plugin.getConfiguration().getGlobalDateFormat();
            message(sender, Message.COMMAND_COOLDOWN_HEADER, "%player%", userName, "%amount%", cooldownDTOS.size());
            cooldownDTOS.forEach(cooldownDTO -> {
                long ms = cooldownDTO.cooldown_value() - System.currentTimeMillis();
                message(sender, Message.COMMAND_COOLDOWN_LINE, "%key%", cooldownDTO.cooldown_name(), "%value%", cooldownDTO.cooldown_value(),
                        "%createdAt%", simpleDateFormat.format(cooldownDTO.created_at()), "%timeLeft%", TimerBuilder.getStringTime(ms), "%player%", userName);
            });
        });

        return CommandResultType.SUCCESS;
    }
}
