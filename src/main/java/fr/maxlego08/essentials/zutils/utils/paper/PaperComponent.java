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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PaperComponent extends PlaceholderUtils implements AdventureComponent {

    private final List<TagPermission> tagPermissions = List.of(
            // Chat
            new TagPermission(Permission.ESSENTIALS_CHAT_COLOR, StandardTags.color()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_CLICK, StandardTags.clickEvent()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_HOVER, StandardTags.hoverEvent()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_GRADIENT, StandardTags.gradient()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_RAINBOW, StandardTags.rainbow()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_NEWLINE, StandardTags.newline()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_RESET, StandardTags.reset()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_FONT, StandardTags.font()), //
            new TagPermission(Permission.ESSENTIALS_CHAT_KEYBIND, StandardTags.keybind()),  //
            new TagPermission(Permission.ESSENTIALS_CHAT_DECORATION, StandardTags.decorations()), //
            // Sign
            new TagPermission(Permission.ESSENTIALS_SIGN_COLOR, StandardTags.color()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_CLICK, StandardTags.clickEvent()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_HOVER, StandardTags.hoverEvent()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_GRADIENT, StandardTags.gradient()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_RAINBOW, StandardTags.rainbow()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_NEWLINE, StandardTags.newline()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_RESET, StandardTags.reset()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_FONT, StandardTags.font()), //
            new TagPermission(Permission.ESSENTIALS_SIGN_KEYBIND, StandardTags.keybind()),  //
            new TagPermission(Permission.ESSENTIALS_SIGN_DECORATION, StandardTags.decorations()) //
    );

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
        updateDisplayName(itemMeta, text, null);
    }

    public void updateDisplayName(ItemMeta itemMeta, String text, Player player) {
        String processedText = papi(text, player);
        String cacheKey = getCacheKey(processedText, player);
        Component component = this.cache.get(cacheKey, () -> {
            return this.MINI_MESSAGE.deserialize(colorMiniMessage(processedText, player)).decoration(TextDecoration.ITALIC, getState(processedText)); // We will force the italics in false, otherwise it will activate for no reason
        });
        itemMeta.displayName(component);
    }

    public void updateLore(ItemMeta itemMeta, List<String> lore, Player offlinePlayer) {
        update(itemMeta, lore, offlinePlayer);
    }

    public void update(ItemMeta itemMeta, List<String> lore, Player offlinePlayer) {
        List<Component> components = lore.stream().map(text -> {
            String result = papi(text, offlinePlayer);
            String cacheKey = getCacheKey(result, offlinePlayer);
            return this.cache.get(cacheKey, () -> {
                return this.MINI_MESSAGE.deserialize(colorMiniMessage(result, offlinePlayer)).decoration(TextDecoration.ITALIC, getState(result)); // We will force the italics in false, otherwise it will activate for no reason
            });
        }).collect(Collectors.toList());
        itemMeta.lore(components);
    }

    @Override
    public Inventory createInventory(String inventoryName, int size, InventoryHolder inventoryHolder) {
        Component component = this.cache.get(inventoryName, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(inventoryName, null)));
        return Bukkit.createInventory(inventoryHolder, size, component);
    }

    private String getCacheKey(String text, Player player) {
        if (player == null) return text;
        boolean hasColorPerm = player.isOp() || player.hasPermission(Permission.ESSENTIALS_CHAT_COLOR.asPermission());
        return text + "_" + hasColorPerm;
    }

    private String colorMiniMessage(String message) {
        return colorMiniMessage(message, null);
    }

    private String colorMiniMessage(String message, Player player) {
        // Check if player has permission for color codes
        boolean hasColorPermission = player == null || player.isOp() || 
                player.hasPermission(Permission.ESSENTIALS_CHAT_COLOR.asPermission());
        
        if (!hasColorPermission) {
            // Strip color codes if no permission
            return stripColorCodes(message);
        }
        
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

    private String stripColorCodes(String message) {
        // Strip Minecraft color codes
        String strippedMessage = message.replaceAll("[&§][0-9a-fA-FkKlLmMnNoOrR]", "");
        // Strip hex color codes
        strippedMessage = strippedMessage.replaceAll("#[a-fA-F0-9]{6}", "");
        // Strip MiniMessage tags
        strippedMessage = strippedMessage.replaceAll("<[^>]+>", "");
        return strippedMessage;
    }

    @Override
    public Component getComponent(String message) {
        return this.cache.get(message, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(message, null)));
    }

    @Override
    public Component getComponent(String message, TagResolver tagResolver) {
        return this.MINI_MESSAGE.deserialize(colorMiniMessage(message, null), tagResolver);
    }

    @Override
    public BossBar createBossBar(String message, BossBar.Color barColor, BossBar.Overlay barStyle) {
        return BossBar.bossBar(getComponent(message), 0, barColor, barStyle);
    }

    private TagResolver getTagResolver(Player player, TagResolver... tagResolvers) {
        TagResolver.Builder builder = TagResolver.builder();

        if (!player.isOp()) {

            Set<TagResolver> resolvers = this.tagPermissions.stream().filter(tagPermission -> player.hasPermission(tagPermission.permission().asPermission())).map(TagPermission::tagResolver).collect(Collectors.toSet());
            builder.resolvers(resolvers);
        } else {

            builder.resolver(StandardTags.defaults());
        }

        builder.resolvers(tagResolvers);
        return builder.build();
    }

    public Component translateText(Player player, String message, TagResolver... tagResolvers) {
        var tagResolver = getTagResolver(player, tagResolvers);
        return MiniMessage.builder().tags(tagResolver).build().deserialize(colorMiniMessage(message, player));
    }

    @Override
    public void sendActionBar(Player sender, String message) {
        String cacheKey = getCacheKey(message, sender);
        Component component = this.cache.get(cacheKey, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(message, sender)));
        sender.sendActionBar(component);
    }

    @Override
    public void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player player) {
            sender.sendMessage(this.MINI_MESSAGE.deserialize(papi(colorMiniMessage(message, player), player)));
        } else {
            Component component = this.cache.get(message, () -> this.MINI_MESSAGE.deserialize(colorMiniMessage(message, null)));
            sender.sendMessage(component);
        }
    }

    public Component getComponentMessage(Message message, Object... args) {
        List<String> strings = message.getMessageAsStringList();
        if (!strings.isEmpty()) {
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
        if (currentLore == null) currentLore = new ArrayList<>();
        currentLore.addAll(lore.stream().map(placeholders::parse).map(text -> this.MINI_MESSAGE.deserialize(colorMiniMessage(text, null))).map(e -> e.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)).toList());
        itemMeta.lore(currentLore);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void sendTitle(Player player, TitleMessage titleMessage, Object... args) {

        String titleText = papi(getMessage(titleMessage.title(), args), player);
        String subtitleText = papi(getMessage(titleMessage.subtitle(), args), player);

        Component title = this.MINI_MESSAGE.deserialize(colorMiniMessage(titleText, player));
        Component subtitle = this.MINI_MESSAGE.deserialize(colorMiniMessage(subtitleText, player));

        player.showTitle(Title.title(title, subtitle, Title.Times.times(Duration.ofMillis(titleMessage.start()), Duration.ofMillis(titleMessage.time()), Duration.ofMillis(titleMessage.end()))));
    }

    @Override
    public void sendBossBar(EssentialsPlugin plugin, Player player, BossBarMessage bossBarMessage) {
        String bossBarText = papi(bossBarMessage.text(), player);
        Component component = this.MINI_MESSAGE.deserialize(colorMiniMessage(bossBarText, player));
        BossBar bossBar = BossBar.bossBar(component, 1f, bossBarMessage.getColor(), bossBarMessage.getOverlay(), bossBarMessage.getFlags());
        player.showBossBar(bossBar);

        new BossBarAnimation(plugin, player, bossBar, bossBarMessage.duration());
    }

    @Override
    public void kick(Player player, String message) {
        Component component = this.MINI_MESSAGE.deserialize(colorMiniMessage(message, player));
        player.kick(component);
    }

    @Override
    public String getItemStackName(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) return "";
        var meta = itemStack.getItemMeta();
        if (!meta.hasDisplayName()) return "";
        return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(meta.displayName()));
    }

    @Override
    public void changeSignColor(SignChangeEvent event) {
        var player = event.getPlayer();

        var miniMessage = MiniMessage.builder().tags(getTagResolver(player)).build();
        var plainTextSerializer = PlainTextComponentSerializer.plainText();

        for (int i = 0; i < event.lines().size(); i++) {
            var line = event.line(i);
            if (line == null) continue;

            var plainText = plainTextSerializer.serialize(line);
            event.line(i, miniMessage.deserialize(colorMiniMessage(plainText, player)));
        }
    }
}
