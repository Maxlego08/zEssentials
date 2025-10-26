package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CommandVaultDeleteSlot extends VCommand {

    public CommandVaultDeleteSlot(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VaultModule.class);
        this.setPermission(Permission.ESSENTIALS_VAULT_DELETE_SLOT);
        this.setDescription(Message.DESCRIPTION_VAULT_DELETE_SLOT);
        this.addSubCommand("delete");
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("vault");
        this.addRequireArg("slot");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        int vaultId = this.argAsInteger(1);
        int slot = this.argAsInteger(2);

        UUID uniqueId = offlinePlayer.getUniqueId();
        String playerName = offlinePlayer.getName() == null ? uniqueId.toString() : offlinePlayer.getName();

        plugin.getScheduler().runAsync(wrappedTask -> {
            boolean removed = plugin.getVaultManager().forceDeleteSlot(uniqueId, vaultId, slot);

            plugin.getScheduler().runNextTick(wrappedTask1 -> {
                if (!removed) {
                    message(this.sender, Message.COMMAND_VAULT_DELETE_SLOT_EMPTY,
                            "%player%", playerName,
                            "%vault%", vaultId,
                            "%slot%", slot);
                    return;
                }

                message(this.sender, Message.COMMAND_VAULT_DELETE_SLOT_SUCCESS,
                        "%player%", playerName,
                        "%vault%", vaultId,
                        "%slot%", slot);
            });
        });

        return CommandResultType.SUCCESS;
    }
}
