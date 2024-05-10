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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandItemLoreAdd extends VCommand {
    public CommandItemLoreAdd(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ItemModule.class);
        this.setPermission(Permission.ESSENTIALS_ITEM_LORE_ADD);
        this.setDescription(Message.DESCRIPTION_ITEM_LORE_ADD);
        this.addSubCommand("add");
        this.addRequireArg("line");
        this.setExtendedArgs(true);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String loreLine = this.getArgs(1);
        if (loreLine.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_ITEM_EMPTY);
            return CommandResultType.DEFAULT;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        List<Component> components = itemMeta.hasLore() ? itemMeta.lore() : new ArrayList<>();
        if (components == null) components = new ArrayList<>();

        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        components.add(paperComponent.getComponent(loreLine).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        itemMeta.lore(components);
        itemStack.setItemMeta(itemMeta);

        message(sender, Message.COMMAND_ITEM_LORE_ADD, "%text%", loreLine);

        return CommandResultType.SUCCESS;
    }
}
