package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.TransformMaterial;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CommandCompact extends VCommand {
    public CommandCompact(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_COMPACT);
        this.setDescription(Message.DESCRIPTION_COMPACT);
        this.addRequireArg("type", (a, b) -> plugin.getConfiguration().getCompactMaterials().stream().map(e -> e.from().name().toLowerCase()).toList());
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Material material = Material.valueOf(this.argAsString(0).toUpperCase());

        Optional<TransformMaterial> optional = this.configuration.getCompactMaterials().stream().filter(e -> e.from().equals(material)).findFirst();
        if (optional.isEmpty()) {

            message(sender, Message.COMMAND_COMPACT_TYPE, "%material%", name(material.name()));
            return CommandResultType.DEFAULT;
        }

        TransformMaterial compactMaterial = optional.get();
        Material newMaterial = compactMaterial.to();

        Inventory inventory = this.player.getInventory();
        int amountOf = count(inventory, material);

        if (amountOf < 9) {
            message(getPlayer(), Message.COMMAND_COMPACT_ERROR, "%item%", name(material.name()));
            return CommandResultType.DEFAULT;
        }

        int realAmount = amountOf / 9;
        removeItems(inventory, new ItemStack(material), realAmount * 9);
        give(this.player, new ItemStack(newMaterial, realAmount));
        message(this.player, Message.COMMAND_COMPACT_SUCCESS, "%amount%", realAmount * 9, "%item%", name(material.name()),
                "%toAmount%", realAmount, "%toItem%", name(newMaterial.name()));

        return CommandResultType.SUCCESS;
    }
}
