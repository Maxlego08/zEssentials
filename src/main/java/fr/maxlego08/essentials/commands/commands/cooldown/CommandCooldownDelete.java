package fr.maxlego08.essentials.commands.commands.cooldown;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.CooldownDTO;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandCooldownDelete extends VCommand {
    public CommandCooldownDelete(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_COOLDOWN_DELETE);
        this.setDescription(Message.DESCRIPTION_COOLDOWN_DELETE);
        this.addSubCommand("delete");
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("cooldown", (sender, args) -> {

            try {

                String playerName = args[1];
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(playerName);
                if (offlinePlayer == null) return new ArrayList<>();

                User user = plugin.getUser(offlinePlayer.getUniqueId());
                return new ArrayList<>(user.getCooldowns().keySet());

            } catch (Exception ignored) {
            }

            return new ArrayList<>();
        });
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        CommandSender sender = this.sender;
        String userName = this.argAsString(0);
        String cooldownName = this.argAsString(1);

        fetchUniqueId(userName, uniqueId -> {

            IStorage iStorage = plugin.getStorageManager().getStorage();
            List<CooldownDTO> cooldownDTOS = iStorage.getCooldowns(uniqueId).stream().filter(dto -> dto.cooldown_value() > System.currentTimeMillis()).toList();

            if (cooldownDTOS.isEmpty()) {
                message(sender, Message.COMMAND_COOLDOWN_EMPTY, "%player%", userName);
                return;
            }

            if (cooldownDTOS.stream().noneMatch(dto -> dto.cooldown_name().equalsIgnoreCase(cooldownName))) {
                message(sender, Message.COMMAND_COOLDOWN_NOT_FOUND, "%cooldown%", cooldownName);
                return;
            }

            iStorage.deleteCooldown(uniqueId, cooldownName);
            plugin.getEssentialsServer().deleteCooldown(uniqueId, cooldownName);
            message(sender, Message.COMMAND_COOLDOWN_DELETE, "%cooldown%", cooldownName, "%player%", userName);
        });

        return CommandResultType.SUCCESS;
    }
}
