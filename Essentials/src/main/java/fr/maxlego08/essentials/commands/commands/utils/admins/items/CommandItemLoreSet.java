package fr.maxlego08.essentials.commands.commands.utils.admins.items;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandItemLoreSet extends VCommand {
    public CommandItemLoreSet(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ITEM_LORE_SET);
        this.setDescription(Message.DESCRIPTION_ITEM_LORE_SET);
        this.addSubCommand("set");
        this.addRequireArg("index");
        this.addRequireArg("line");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        int index = this.argAsInteger(0);
        String loreLine = this.getArgs(2);
        if (loreLine.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_ITEM_EMPTY);
            return CommandResultType.DEFAULT;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        List<Component> components = itemMeta.hasLore() ? itemMeta.lore() : new ArrayList<>();
        if (components == null) components = new ArrayList<>();

        if (components.size() < index) {
            message(sender, Message.COMMAND_ITEM_LORE_SET_ERROR, "%line%", index);
            return CommandResultType.DEFAULT;
        }


        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        components.set(index - 1, paperComponent.getComponent(loreLine).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

        itemMeta.lore(components);
        itemStack.setItemMeta(itemMeta);

        message(sender, Message.COMMAND_ITEM_LORE_SET, "%text%", loreLine, "%line%", index);

        return CommandResultType.SUCCESS;
    }
}
