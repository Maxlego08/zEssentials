package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.ChatCooldown;
import fr.maxlego08.essentials.api.utils.ChatFormat;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.CooldownLimit;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class ChatModule extends ZModule {

    private final Pattern urlPattern = Pattern.compile("(https?://[\\w-\\.]+(\\:[0-9]+)?(/[\\w- ./?%&=]*)?)", Pattern.CASE_INSENSITIVE);
    private final CooldownLimit limit = new CooldownLimit();
    private final List<ChatCooldown> chatCooldowns = new ArrayList<>();
    private final List<ChatFormat> chatFormats = new ArrayList<>();
    private String alphanumericRegex;
    private String linkRegex;
    private String itemaddersFontRegex;
    private Pattern alphanumericPattern;
    private Pattern linkPattern;
    private Pattern fontPattern;
    private boolean enableAlphanumericRegex;
    private boolean enableLinkRegex;
    private boolean enableItemaddersFontRegex;
    private boolean enableChatDynamicCooldown;
    private boolean enableSameMessageCancel;
    private boolean enableChatFormat;
    private boolean enableLinkTransform;
    private int chatCooldownMax;
    private String defaultChatFormat;
    private String moderatorAction;
    private String linkTransform;
    private long[] chatCooldownArray;


    public ChatModule(ZEssentialsPlugin plugin) {
        super(plugin, "chat");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.limit.setSamples(this.chatCooldownMax);
        this.alphanumericPattern = Pattern.compile(this.alphanumericRegex);
        this.linkPattern = Pattern.compile(this.linkRegex);
        this.fontPattern = Pattern.compile(this.itemaddersFontRegex);
        this.chatCooldownArray = this.chatCooldowns.stream().flatMapToLong(cooldown -> LongStream.of(cooldown.cooldown(), cooldown.messages())).toArray();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTalk(AsyncChatEvent event) {

        Player player = event.getPlayer();
        User user = plugin.getUser(player.getUniqueId());

        if (user == null) {
            cancelEvent(event, Message.CHAT_ERROR);
            return;
        }

        if (ConfigStorage.chatDisable && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_DISABLE)) {
            cancelEvent(event, Message.CHAT_DISABLE);
            return;
        }

        String message = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());

        if (this.enableAlphanumericRegex && !this.alphanumericPattern.matcher(message).find() && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_ALPHANUMERIC)) {
            cancelEvent(event, Message.CHAT_ALPHANUMERIC_REGEX);
            return;
        }

        if (this.enableLinkRegex && this.linkPattern.matcher(message.replace(" ", "")).find() && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_LINK)) {
            cancelEvent(event, Message.CHAT_LINK);
            return;
        }

        double cooldown = handleCooldown(player);
        if (this.enableChatDynamicCooldown && cooldown > 0 && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_DYNAMIC_COOLDOWN)) {
            cancelEvent(event, Message.CHAT_COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(cooldown));
            return;
        }

        String lastMessage = user.getLastMessage();
        if (this.enableSameMessageCancel && message.equalsIgnoreCase(lastMessage) && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_SAME_MESSAGE)) {
            cancelEvent(event, Message.CHAT_SAME, "%cooldown%", TimerBuilder.getStringTime(cooldown));
            return;
        }

        user.setLastMessage(message);

        if (!this.enableChatFormat) return;

        if (this.enableLinkTransform && hasPermission(player, Permission.ESSENTIALS_CHAT_LINK)) {
            message = transformUrlsToMiniMessage(message);
        }

        String chatFormat = papi(getChatFormat(player), player);
        Tag tag = Tag.inserting(this.componentMessage.translateText(player, message));

        event.renderer((source, sourceDisplayName, ignoredMessage, viewer) -> {

            boolean isModerator = viewer instanceof Player playerViewer && hasPermission(playerViewer, Permission.ESSENTIALS_CHAT_MODERATOR);

            return getComponentMessage(chatFormat, TagResolver.resolver("message", tag), "%displayName%", player.getDisplayName(), "%player%", player.getName(), "%moderator_action%", isModerator ? papi(getMessage(this.moderatorAction, "%player%", player.getName()), (Player) viewer) : "");
        });
    }

    private String getChatFormat(Player player) {
        return this.chatFormats.stream().filter(chatFormat -> player.hasPermission(chatFormat.permission())).sorted(Comparator.comparingInt(ChatFormat::priority).reversed()).map(ChatFormat::format).findFirst().orElse(this.defaultChatFormat);
    }

    private double handleCooldown(Player player) {

        long wait;

        synchronized (this.limit) {
            wait = this.limit.limited(player.getUniqueId(), this.chatCooldownArray);
            if (wait == 0L) this.limit.add(player.getUniqueId());
        }

        return wait != 0L ? wait : 0.0;
    }

    private void cancelEvent(AsyncChatEvent event, Message message, Object... objects) {
        event.setCancelled(true);
        message(event.getPlayer(), message, objects);
    }

    private String transformUrlsToMiniMessage(String input) {
        Matcher matcher = urlPattern.matcher(input);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String url = matcher.group(1);
            matcher.appendReplacement(result, getMessage(this.linkTransform, "%url%", url));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
