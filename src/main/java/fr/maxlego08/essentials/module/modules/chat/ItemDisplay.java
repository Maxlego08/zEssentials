package fr.maxlego08.essentials.module.modules.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.chat.ChatDisplayException;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDisplay extends ZUtils implements ChatDisplay {

    private static final String DEFAULT_ITEM_NAME_REGEX = "^[a-zA-Z0-9_.?!^¨%ù*&é\"#'{(\\[-|èêë`\\\\çà)\\]=}ûî+<>:²€$/\\-,-â@;ô ]+$";

    private final EssentialsPlugin plugin;
    private final Pattern pattern;
    private final Pattern itemNamePattern;
    private final String result;
    private final String permission;

    public ItemDisplay(EssentialsPlugin plugin, String regex, String result, String permission, String itemNameRegex) {
        this.plugin = plugin;
        this.pattern = Pattern.compile(regex);
        this.itemNamePattern = Pattern.compile(itemNameRegex != null && !itemNameRegex.isEmpty() ? itemNameRegex : DEFAULT_ITEM_NAME_REGEX);
        this.result = result;
        this.permission = permission;
    }

    @Override
    public String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message) {

        ItemStack itemStack = sender.getInventory().getItemInMainHand();
        if (itemStack.isEmpty()) {
            return message;
        }
        Component itemName = Component.translatable(itemStack);
        int amount = itemStack.getAmount();

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            itemName = itemStack.getItemMeta().displayName();
        }

        String plainName = PlainTextComponentSerializer.plainText().serialize(itemName);
        if (!plainName.isEmpty() && !this.itemNamePattern.matcher(plainName).matches()) {
            throw new ChatDisplayException(Message.CHAT_ITEM_FORBIDDEN_CHARACTERS);
        }

        Matcher matcher = this.pattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();

        String name = "item";
        while (matcher.find()) matcher.appendReplacement(formattedMessage, "<" + name + ">");

        Component component = adventureComponent.getComponent(this.result, TagResolver.builder().resolver(Placeholder.component("item", itemName)).resolver(Placeholder.component("amount", Component.text(amount))).build());

        component = component.hoverEvent(itemStack.asHoverEvent());
        String code = plugin.getModuleManager().getModule(ChatModule.class).createHoverItemStack(sender, itemStack);
        component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/showitem " + code));
        builder.resolver(Placeholder.component(name, component));

        matcher.appendTail(formattedMessage);
        return formattedMessage.toString();
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(this.permission);
    }
}
