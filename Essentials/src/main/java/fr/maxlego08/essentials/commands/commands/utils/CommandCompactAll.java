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

import java.util.List;

public class CommandCompactAll extends VCommand {
    public CommandCompactAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_COMPACT_ALL);
        this.setDescription(Message.DESCRIPTION_COMPACT_ALL);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        List<TransformMaterial> compactMaterials = this.configuration.getCompactMaterials();
        compactMaterials.forEach(compactMaterial -> {

            Material newMaterial = compactMaterial.to();
            Material material = compactMaterial.from();

            Inventory inventory = this.player.getInventory();
            int amountOf = count(inventory, material);

            if (amountOf < 9) return;

            int realAmount = amountOf / 9;
            removeItems(inventory, new ItemStack(material), realAmount * 9);
            give(this.player, new ItemStack(newMaterial, realAmount));

        });

        message(sender, Message.COMMAND_COMPACT_SUCCESS_ALL);

        return CommandResultType.SUCCESS;
    }
}
