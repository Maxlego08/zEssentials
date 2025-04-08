package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.inventory.EquipmentSlot;

public class ArmorPlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        placeholder.register("armor_name_", (player, args) -> {
            try {
                var itemStack = player.getInventory().getItem(EquipmentSlot.valueOf(args.toUpperCase()));
                if (itemStack.getType().isAir()) return "";
                return plugin.getComponentMessage().getItemStackName(itemStack);
            } catch (Exception exception) {
                return exception.getMessage();
            }
        }, "Returns the name of the player’s armor, without the color", "armor slot");

    }
}
