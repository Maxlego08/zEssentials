package fr.maxlego08.essentials.zutils.utils.paper;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.cache.SimpleCache;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import fr.maxlego08.essentials.api.messages.messages.TitleMessage;
import fr.maxlego08.essentials.api.utils.TagPermission;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.zutils.utils.BossBarAnimation;
import fr.maxlego08.essentials.zutils.utils.MessageUtils;
import fr.maxlego08.essentials.zutils.utils.PlaceholderUtils;
import fr.maxlego08.menu.api.utils.Placeholders;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PaperComponent extends PlaceholderUtils implements AdventureComponent {

    private final List<TagPermission> tagPermissions = List.of(new TagPermission(Permission.ESSENTIALS_CHAT_COLOR, StandardTags.color()), new TagPermission(Permission.ESSENTIALS_CHAT_CLICK, StandardTags.clickEvent()), new TagPermission(Permission.ESSENTIALS_CHAT_HOVER, StandardTags.hoverEvent()), new TagPermission(Permission.ESSENTIALS_CHAT_GRADIENT, StandardTags.gradient()), new TagPermission(Permission.ESSENTIALS_CHAT_RAINBOW, StandardTags.rainbow()), new TagPermission(Permission.ESSENTIALS_CHAT_NEWLINE, StandardTags.newline()), new TagPermission(Permission.ESSENTIALS_CHAT_RESET, StandardTags.reset()), new TagPermission(Permission.ESSENTIALS_CHAT_FONT, StandardTags.font()), new TagPermission(Permission.ESSENTIALS_CHAT_KEYBIND, StandardTags.keybind()), new TagPermission(Permission.ESSENTIALS_CHAT_DECORATION, StandardTags.decorations()));

    private final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(TagResolver.builder().resolver(StandardTags.defaults()).build()).build();
    private final Map<String, String> COLORS_MAPPINGS = new HashMap<>();
    private final SimpleCache<String, Component> cache = new SimpleCache<>();

    public PaperComponent() {
        this.COLORS_MAPPINGS.put("0", "black");
        this.COLORS_MAPPINGS.put("1", "dark_blue");
        this.COLORS_MAPPINGS.put("2", "dark_green");
        this.COLORS_MAPPINGS.put("3", "dark_aqua");
        this.COLORS_MAPPINGS.put("4", "dark_red");
        this.COLORS_MAPPINGS.put("5", "dark_purple");
        this.COLORS_MAPPINGS.put("6", "gold");
        this.COLORS_MAPPINGS.put("7", "gray");
        this.COLORS_MAPPINGS.put("8", "dark_gray");
        this.COLORS_MAPPINGS.put("9", "blue");
        this.COLORS_MAPPINGS.put("a", "green");
        this.COLORS_MAPPINGS.put("b", "aqua");
        this.COLORS_MAPPINGS.put("c", "red");
        this.COLORS_MAPPINGS.put("d", "light_purple");
        this.COLORS_MAPPINGS.put("e", "yellow");
        this.COLORS_MAPPINGS.put("f", "white");
        this.COLORS_MAPPINGS.put("k", "obfuscated");
        this.COLORS_MAPPINGS.put("l", "bold");
        this.COLORS_MAPPINGS.put("m", "strikethrough");
        this.COLORS_MAPPINGS.put("n", "underlined");
        this.COLORS_MAPPINGS.put("o", "italic");
        this.COLORS_MAPPINGS.put("r", "reset");
    }

    private TextDecoration.State getState(String text) {
        return text.contains("&o") || text.contains("<i>") || text.contains("<em>") || text.contains("<italic>") ? TextDecoration.State.TRUE : TextDecoration.State.FALSE;
    }

    private void updateDisplayName(ItemMeta itemMeta, String text) {
        Component component = this.cache.get(text, () -> {
            return this.MINI_MESSAGE.deserialize(colorMiniMessage(text)).decoration(TextDecoration.ITALIC, getState(text)); // We will force the italics in false, otherwise it will activate for no reason
        });
        itemMeta.displayName(component);
    }

    public void updateDisplayName(ItemMeta itemMeta, String text, Player player) {
        updateDisplayName(itemMeta, papi(text, player));
    }

    public void updateLore(ItemMeta itemMeta, List<String> lore, Player offlinePlayer) {
        update(itemMeta, lore, offlinePlayer);
    }

    public void update(ItemMeta itemMeta, List<String> lore, Player offlinePlayer) {
        List<Component> components = lore.stream().map(text -> {
            String result = papi(text, offlinePlayer);
            return this.cache.get(result, () -> {
                return this.MINI_MESSAGE.deserialize(colorMiniMessage(result)).decoration(TextDecoration.ITALIC, getState(result)); // We will force the italics in false, otherwise it will activate for no reason
            });
        }).collect(Collectors.toList());
        itemMeta.lore(components);
    }

    @Override
    public Inventory createInventory(String inventoryName, int size, InventoryHolder inventoryHolder) {
        Component component = this.cache.get(inventoryName, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(inventoryName)));
        return Bukkit.createInventory(inventoryHolder, size, component);
    }

    private String colorMiniMessage(String message) {
        StringBuilder stringBuilder = new StringBuilder();

        Pattern pattern = Pattern.compile("(?<!<)(?<!:)#([a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            matcher.appendReplacement(stringBuilder, "<$0>");
        }
        matcher.appendTail(stringBuilder);

        String newMessage = stringBuilder.toString();

        for (Map.Entry<String, String> entry : this.COLORS_MAPPINGS.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            newMessage = newMessage.replace("&" + key, "<" + value + ">");
            newMessage = newMessage.replace("§" + key, "<" + value + ">");
            newMessage = newMessage.replace("&" + key.toUpperCase(), "<" + value + ">");
            newMessage = newMessage.replace("§" + key.toUpperCase(), "<" + value + ">");
        }

        return newMessage;
    }

    @Override
    public Component getComponent(String message) {
        return this.cache.get(message, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(message)));
    }

    @Override
    public Component getComponent(String message, TagResolver tagResolver) {
        return this.MINI_MESSAGE.deserialize(colorMiniMessage(message), tagResolver);
    }

    @Override
    public BossBar createBossBar(String message, BossBar.Color barColor, BossBar.Overlay barStyle) {
        return BossBar.bossBar(getComponent(message), 0, barColor, barStyle);
    }

    public Component translateText(Player player, String message, TagResolver... tagResolvers) {

        TagResolver.Builder builder = TagResolver.builder();

        if (!player.isOp()) {

            List<TagResolver> resolvers = this.tagPermissions.stream().filter(tagPermission -> player.hasPermission(tagPermission.permission().asPermission())).map(TagPermission::tagResolver).toList();
            builder.resolvers(resolvers);
        } else {

            builder.resolver(StandardTags.defaults());
        }

        builder.resolvers(tagResolvers);

        return MiniMessage.builder().tags(builder.build()).build().deserialize(colorMiniMessage(message));
    }

    @Override
    public void sendActionBar(Player sender, String message) {
        Component component = this.cache.get(message, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(message)));
        sender.sendActionBar(component);
    }

    @Override
    public void sendMessage(CommandSender sender, String message) {
        Component component = this.cache.get(message, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(message)));
        sender.sendMessage(component);
    }

    public Component getComponentMessage(Message message, Object... args) {
        List<String> strings = message.getMessageAsStringList();
        if (strings.size() > 0) {
            TextComponent.Builder component = Component.text();
            strings.forEach(currentMessage -> {
                component.append(getComponent(getMessage(currentMessage, args)));
                component.append(Component.text("\n"));
            });
            return component.build();
        }
        return getComponent(getMessage(message.getMessageAsString(), args));
    }

    public Component getComponentMessage(String message, TagResolver tagResolver, Object... args) {
        return getComponent(getMessage(message, args), tagResolver);
    }

    protected String getMessage(String message, Object... args) {
        return MessageUtils.getString(message, args);
    }

    @Override
    public void addToLore(ItemStack itemStack, List<String> lore, Placeholders placeholders) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<Component> currentLore = itemMeta.hasLore() ? itemMeta.lore() : new ArrayList<>();
        currentLore.addAll(lore.stream().map(placeholders::parse).map(this::getComponent).map(e -> e.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)).toList());
        itemMeta.lore(currentLore);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void sendTitle(Player player, TitleMessage titleMessage, Object... args) {

        Component title = getComponent(papi(getMessage(titleMessage.title(), args), player));
        Component subtitle = getComponent(papi(getMessage(titleMessage.subtitle(), args), player));

        player.showTitle(Title.title(title, subtitle, Title.Times.times(Duration.ofMillis(titleMessage.start()), Duration.ofMillis(titleMessage.time()), Duration.ofMillis(titleMessage.end()))));
    }

    @Override
    public void sendBossBar(EssentialsPlugin plugin, Player player, BossBarMessage bossBarMessage) {
        BossBar bossBar = BossBar.bossBar(getComponent(papi(bossBarMessage.text(), player)), 1f, bossBarMessage.getColor(), bossBarMessage.getOverlay(), bossBarMessage.getFlags());
        player.showBossBar(bossBar);

        new BossBarAnimation(plugin, player, bossBar, bossBarMessage.duration());
    }
}
