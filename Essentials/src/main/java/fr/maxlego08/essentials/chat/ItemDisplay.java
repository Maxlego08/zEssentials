package fr.maxlego08.essentials.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDisplay extends ZUtils implements ChatDisplay {

    private final EssentialsPlugin plugin;
    private final Pattern pattern;
    private final String result;
    private final String permission;

    public ItemDisplay(EssentialsPlugin plugin, String regex, String result, String permission) {
        this.plugin = plugin;
        this.pattern = Pattern.compile(regex);
        this.result = result;
        this.permission = permission;
    }

    @Override
    public String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message) {

        ItemStack itemStack = sender.getInventory().getItemInMainHand();
        Component itemName = Component.translatable(itemStack);
        int amount = itemStack.getAmount();

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            itemName = itemStack.getItemMeta().displayName();
        }

        Matcher matcher = this.pattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();

        String name = "item";
        while (matcher.find()) matcher.appendReplacement(formattedMessage, "<" + name + ">");

        Component component = adventureComponent.getComponent(this.result, TagResolver.builder().resolver(Placeholder.component("item", itemName)).resolver(Placeholder.component("amount", Component.text(amount))).build());

        String nbt = plugin.getNmsUtils().getItemStackTags(itemStack);
        component = component.hoverEvent(HoverEvent.showItem(itemStack.getType(), amount, BinaryTagHolder.binaryTagHolder(nbt)));
        builder.resolver(Placeholder.component(name, component));

        matcher.appendTail(formattedMessage);
        return formattedMessage.toString();
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(this.permission);
    }
}
