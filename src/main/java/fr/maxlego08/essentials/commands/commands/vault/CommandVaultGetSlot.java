package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class CommandVaultGetSlot extends VCommand {

    public CommandVaultGetSlot(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VaultModule.class);
        this.setPermission(Permission.ESSENTIALS_VAULT_GET_SLOT);
        this.setDescription(Message.DESCRIPTION_VAULT_GET_SLOT);
        this.addSubCommand("get");
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("vault");
        this.addRequireArg("slot");
        this.addOptionalArg("give");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        int vaultId = this.argAsInteger(1);
        int slot = this.argAsInteger(2);
        boolean give = this.argAsBoolean(3, false);

        UUID uniqueId = offlinePlayer.getUniqueId();
        String playerName = offlinePlayer.getName() == null ? uniqueId.toString() : offlinePlayer.getName();
        var player = this.sender instanceof Player currentPlayer ? currentPlayer : null;

        plugin.getScheduler().runAsync(wrappedTask -> {
            Optional<VaultItemDTO> optional = plugin.getVaultManager().getVaultItem(uniqueId, vaultId, slot);

            plugin.getScheduler().runNextTick(wrappedTask1 -> {
                if (optional.isEmpty()) {
                    message(this.sender, Message.COMMAND_VAULT_GET_SLOT_EMPTY, "%player%", playerName, "%vault%", vaultId, "%slot%", slot);
                    return;
                }

                VaultItemDTO vaultItemDTO = optional.get();
                ItemStack itemStack = ItemStackUtils.deserializeItemStack(vaultItemDTO.item());
                String itemName;

                if (itemStack == null) {
                    itemName = "unknown";
                } else if (itemStack.hasItemMeta() && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                    itemName = itemStack.getItemMeta().getDisplayName();
                } else {
                    itemName = itemStack.getType().name();
                }

                message(this.sender, Message.COMMAND_VAULT_GET_SLOT_SUCCESS, "%player%", playerName, "%vault%", vaultId, "%slot%", slot, "%amount%", String.valueOf(vaultItemDTO.quantity()), "%item%", itemName);

                if (give && player != null && itemStack != null) {
                    player.getInventory().addItem(itemStack);
                }
            });
        });

        return CommandResultType.SUCCESS;
    }
}
