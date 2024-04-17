package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.ChatCooldown;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.CooldownLimit;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class ChatModule extends ZModule {

    private final CooldownLimit limit = new CooldownLimit();
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
    private int chatCooldownMax;
    private List<ChatCooldown> chatCooldowns = new ArrayList<>();
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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTalk(AsyncChatEvent event) {

        User user = plugin.getUser(event.getPlayer().getUniqueId());
        if (user == null) {
            cancelEvent(event, Message.CHAT_ERROR);
            return;
        }

        if (ConfigStorage.chatDisable && !hasPermission(event.getPlayer(), Permission.ESSENTIALS_CHAT_BYPASS_DISABLE)) {
            cancelEvent(event, Message.CHAT_DISABLE);
            return;
        }

        String message = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());

        if (this.enableAlphanumericRegex && !this.alphanumericPattern.matcher(message).find() && !hasPermission(event.getPlayer(), Permission.ESSENTIALS_CHAT_BYPASS_ALPHANUMERIC)) {
            cancelEvent(event, Message.CHAT_ALPHANUMERIC_REGEX);
            return;
        }

        if (this.enableLinkRegex && this.linkPattern.matcher(message.replace(" ", "")).find() && !hasPermission(event.getPlayer(), Permission.ESSENTIALS_CHAT_BYPASS_LINK)) {
            cancelEvent(event, Message.CHAT_LINK);
            return;
        }

        double cooldown = handleCooldown();
        if (this.enableChatDynamicCooldown && cooldown > 0 && !hasPermission(event.getPlayer(), Permission.ESSENTIALS_CHAT_BYPASS_DYNAMIC_COOLDOWN)) {
            cancelEvent(event, Message.CHAT_COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(cooldown));
            return;
        }

        String lastMessage = user.getLastMessage();
        if (this.enableSameMessageCancel && message.equalsIgnoreCase(lastMessage) && !hasPermission(event.getPlayer(), Permission.ESSENTIALS_CHAT_BYPASS_SAME_MESSAGE)) {
            cancelEvent(event, Message.CHAT_SAME, "%cooldown%", TimerBuilder.getStringTime(cooldown));
            return;
        }

        user.setLastMessage(message);


    }

    private double handleCooldown() {

        long wait;

        synchronized (this.limit) {
            wait = this.limit.limited(this.chatCooldownArray);
            if (wait == 0L) this.limit.add();
        }

        return wait != 0L ? wait : 0.0;
    }

    private void cancelEvent(AsyncChatEvent event, Message message, Object... objects) {
        event.setCancelled(true);
        message(event.getPlayer(), message, objects);
    }
}
