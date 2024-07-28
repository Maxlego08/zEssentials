package fr.maxlego08.essentials.commands.commands.items;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandGive extends VCommand {
    public CommandGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ItemModule.class);
        this.setPermission(Permission.ESSENTIALS_GIVE);
        this.setDescription(Message.DESCRIPTION_GIVE);
        this.addRequirePlayerNameArg();
        this.addRequireArg("item", (sender, args) -> {
            List<String> materials = new ArrayList<>(plugin.getMaterials().stream().map(Material::name).map(String::toLowerCase).toList());
            materials.addAll(plugin.getModuleManager().getModule(ItemModule.class).getItemsName());
            return materials;
        });
        this.addOptionalArg("amount", (sender, args) -> Arrays.asList("1", "64", "full"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String itemName = this.argAsString(1);
        String amount = this.argAsString(2, "1");

        var module = plugin.getModuleManager().getModule(ItemModule.class);
        if (amount.equalsIgnoreCase("full")) {

            module.giveFullInventory(sender, player, itemName);
        } else {
            int amountInteger = this.argAsInteger(2, 1);
            module.give(sender, player, itemName, amountInteger);
        }

        return CommandResultType.SUCCESS;
    }
}
