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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CommandItemLoreSet extends VCommand {

    private final NamespacedKey loreLineRaw;

    public CommandItemLoreSet(EssentialsPlugin plugin) {
        super(plugin);
        this.loreLineRaw = new NamespacedKey(plugin, "lore-line-raw");
        this.setModule(ItemModule.class);
        this.setPermission(Permission.ESSENTIALS_ITEM_LORE_SET);
        this.setDescription(Message.DESCRIPTION_ITEM_LORE_SET);
        this.addSubCommand("set");
        this.addRequireArg("index", (sender, args) -> {
            if (sender instanceof Player player) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasLore()) {
                    var lore = itemMeta.lore();
                    if (lore == null) return new ArrayList<>();
                    return IntStream.range(1, lore.size() + 1).mapToObj(String::valueOf).toList();
                }
            }
            return new ArrayList<>();
        });
        this.addRequireArg("line", (sender, args) -> {
            if (sender instanceof Player player) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasLore()) {
                    var lore = itemMeta.lore();
                    if (lore == null) return new ArrayList<>();
                    try {

                        int index = Integer.parseInt(args[1]) - 1;

                        if (itemMeta.getPersistentDataContainer().has(this.loreLineRaw, PersistentDataType.STRING)) {
                            var line = itemMeta.getPersistentDataContainer().getOrDefault(this.loreLineRaw, PersistentDataType.STRING, "").split(";");
                            return Arrays.asList(line[index]);
                        }

                        var component = lore.get(index);
                        return List.of(component == null ? "" : colorReverse(LegacyComponentSerializer.legacyAmpersand().serialize(component)));
                    } catch (Exception ignored) {
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
        if (components.size() < index) {
            for (int i = components.size(); i < index; i++) {
                components.add(Component.text(""));
            }
        }

        components.set(index - 1, paperComponent.getComponent(loreLine).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        itemMeta.lore(components);

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();

        int newSize = itemMeta.lore() == null ? 1 : itemMeta.lore().size();
        String[] rawLoreLines = dataContainer.has(loreLineRaw, PersistentDataType.STRING) ? dataContainer.getOrDefault(loreLineRaw, PersistentDataType.STRING, "").split(";") : new String[newSize];

        if (rawLoreLines.length < newSize) {
            String[] newRawLoreLines = new String[newSize];
            System.arraycopy(rawLoreLines, 0, newRawLoreLines, 0, rawLoreLines.length);
            rawLoreLines = newRawLoreLines;
        }

        for (int i = 0; i != components.size(); i++) {
            if (rawLoreLines[i] == null) {
                rawLoreLines[i] = colorReverse(LegacyComponentSerializer.legacyAmpersand().serialize(components.get(i)));
            }
        }

        rawLoreLines[index - 1] = loreLine;

        String rawLoreCombined = String.join(";", rawLoreLines);

        dataContainer.set(loreLineRaw, PersistentDataType.STRING, rawLoreCombined);

        itemStack.setItemMeta(itemMeta);

        message(sender, Message.COMMAND_ITEM_LORE_SET, "%text%", loreLine, "%line%", index);

        return CommandResultType.SUCCESS;
    }
}
