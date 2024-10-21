package fr.maxlego08.essentials.commands.commands.items;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CommandItemName extends VCommand {

    private final NamespacedKey itemNameRaw;

    public CommandItemName(EssentialsPlugin plugin) {
        super(plugin);

        this.itemNameRaw = new NamespacedKey(plugin, "item-name-raw");

        this.setModule(ItemModule.class);
        this.setPermission(Permission.ESSENTIALS_ITEM_NAME);
        this.setDescription(Message.DESCRIPTION_ITEM_NAME);
        this.addOptionalArg("name", (sender, args) -> {
            if (sender instanceof Player player) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.hasItemMeta()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta.hasDisplayName()) {
                        if (itemMeta.getPersistentDataContainer().has(this.itemNameRaw, PersistentDataType.STRING)) {
                            return List.of(itemMeta.getPersistentDataContainer().getOrDefault(this.itemNameRaw, PersistentDataType.STRING, ""));
                        }
                        var component = itemMeta.displayName();
                        return List.of(component == null ? "" : colorReverse(LegacyComponentSerializer.legacyAmpersand().serialize(component)));
                    }
                }
            }
            return new ArrayList<>();
        });
        this.setExtendedArgs(true);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String itemName = this.getArgs(0);
        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_ITEM_EMPTY);
            return CommandResultType.DEFAULT;
        }

        boolean isReset = itemName.isEmpty();
        ItemMeta itemMeta = itemStack.getItemMeta();

        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        Component component = isReset ? null : paperComponent.getComponent(itemName);
        if (component != null) {
            component = component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        }
        itemMeta.displayName(component);
        itemMeta.getPersistentDataContainer().set(this.itemNameRaw, PersistentDataType.STRING, itemName);

        itemStack.setItemMeta(itemMeta);

        message(sender, isReset ? Message.COMMAND_ITEM_CLEAR : Message.COMMAND_ITEM_SET, "%name%", itemName);

        return CommandResultType.SUCCESS;
    }
}
