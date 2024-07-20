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

public class CommandFurnace extends VCommand {
    public CommandFurnace(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_FURNACE);
        this.setDescription(Message.DESCRIPTION_FURNACE);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        Material material = itemStack.getType();
        Optional<TransformMaterial> optional = this.configuration.getSmeltableMaterials().stream().filter(e -> e.from().equals(material)).findFirst();
        if (optional.isEmpty()) {

            message(sender, Message.COMMAND_FURNACE_TYPE, "%material%", name(material.name()));
            return CommandResultType.DEFAULT;
        }

        TransformMaterial compactMaterial = optional.get();
        Material newMaterial = compactMaterial.to();

        Inventory inventory = this.player.getInventory();
        int realAmount = count(inventory, material);

        if (realAmount < 0) {
            message(getPlayer(), Message.COMMAND_FURNACE_ERROR, "%item%", name(material.name()));
            return CommandResultType.DEFAULT;
        }

        removeItems(inventory, new ItemStack(material), realAmount);
        this.plugin.give(this.player, new ItemStack(newMaterial, realAmount));
        message(this.player, Message.COMMAND_FURNACE_SUCCESS, "%amount%", realAmount, "%item%", name(material.name()), "%toAmount%", realAmount, "%toItem%", name(newMaterial.name()));

        return CommandResultType.SUCCESS;
    }
}
