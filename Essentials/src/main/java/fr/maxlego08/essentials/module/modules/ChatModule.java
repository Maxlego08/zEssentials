package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.cache.ExpiringCache;
import fr.maxlego08.essentials.api.chat.ChatCooldown;
import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.chat.ChatFormat;
import fr.maxlego08.essentials.api.chat.ChatPlaceholder;
import fr.maxlego08.essentials.api.chat.ChatResult;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.database.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.event.events.user.UserJoinEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.DynamicCooldown;
import fr.maxlego08.essentials.chat.CommandDisplay;
import fr.maxlego08.essentials.chat.CustomDisplay;
import fr.maxlego08.essentials.chat.ItemDisplay;
import fr.maxlego08.essentials.chat.PlayerPingDisplay;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class ChatModule extends ZModule {

    private final List<ChatDisplay> chatDisplays = new ArrayList<>();
    private final ExpiringCache<UUID, List<ChatMessageDTO>> chatMessagesCache = new ExpiringCache<>(1000 * 60);
    private final Pattern urlPattern = Pattern.compile("(https?://[\\w-\\.]+(\\:[0-9]+)?(/[\\w- ./?%&=]*)?)", Pattern.CASE_INSENSITIVE);
    private final List<ChatCooldown> chatCooldowns = new ArrayList<>();
    private final List<ChatFormat> chatFormats = new ArrayList<>();
    private final List<ChatPlaceholder> chatPlaceholders = new ArrayList<>();
    private final String alphanumericRegex = "^[a-zA-Z0-9_.?!^¨%ù*&é\"#'{(\\[-|èêë`\\\\çà)\\]=}ûî+<>:²€$/\\-,-â@;ô ]+$";
    private final String linkRegex = "[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)";
    private final String itemaddersFontRegex = "(?<=:)(.*?)(?=\\s*\\:)";
    private final String defaultChatFormat = "<hover:show_text:'&cReport this message'><click:run_command:'/report %player% chat'>⚠</click></hover> %moderator_action%<#ffffff><hover:show_text:'#ffd353ℹ Informations#3f3f3f:<newline>#3f3f3f• &7Money#3f3f3f: #4cd5ff%zessentials_user_formatted_balance_money%<newline>#3f3f3f• &7Coins#3f3f3f: #4cd5ff%zessentials_user_formatted_balance_coins%<newline><newline>&f➥ &7Click for more information'>%player%</hover> <#333333>» <gray><click:suggest_command:'/msg %player% '><hover:show_text:'&fSend a private message'><message></hover></click>";
    private final String moderatorAction = "<hover:show_text:'<#ff8888>Punish the player'><click:run_command:'/sc %player%'><#ff8888>✗</click></hover> ";
    private final String linkTransform = "<hover:show_text:'&fOpen the link'><click:open_url:'%url%'>%url%</click></hover>";
    private final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat;
    private Pattern alphanumericPattern;
    private Pattern linkPattern;
    private Pattern fontPattern;
    private boolean enableAlphanumericRegex;
    private boolean enableLinkRegex;
    private boolean enableItemaddersFontRegex;
    private boolean enableChatDynamicCooldown;
    private boolean enableSameMessageCancel;
    private boolean enableChatFormat;
    private boolean enablePing;
    private boolean enableLinkTransform;
    private boolean enableChatMessages;
    private int chatCooldownMax;
    private long[] chatCooldownArray;
    private boolean enablePlayerPingSound;
    private Sound playerPingSound;
    private String playerPingColor;
    private String playerPingColorOther;
    private float playerPingSoundVolume;
    private float playerPingSoundPitch;


    public ChatModule(ZEssentialsPlugin plugin) {
        super(plugin, "chat");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.alphanumericPattern = Pattern.compile(this.alphanumericRegex);
        this.linkPattern = Pattern.compile(this.linkRegex);
        this.fontPattern = Pattern.compile(this.itemaddersFontRegex);
        this.chatCooldownArray = this.chatCooldowns.stream().flatMapToLong(cooldown -> LongStream.of(cooldown.cooldown(), cooldown.messages())).toArray();
        this.simpleDateFormat = new SimpleDateFormat(this.dateFormat);

        this.chatDisplays.clear();
        if (enablePing) {
            this.chatDisplays.add(new PlayerPingDisplay(playerPingColor, playerPingColorOther, playerPingSound, playerPingSoundVolume, playerPingSoundPitch));
        }

        this.chatPlaceholders.forEach(chatPlaceholder -> this.chatDisplays.add(new CustomDisplay(chatPlaceholder.name(), chatPlaceholder.regex(), chatPlaceholder.result(), chatPlaceholder.permission())));

        YamlConfiguration configuration = getConfiguration();
        if (configuration.getBoolean("item-placeholder.enable")) {
            this.chatDisplays.add(new ItemDisplay(plugin, configuration.getString("item-placeholder.regex"), configuration.getString("item-placeholder.result"), configuration.getString("item-placeholder.permission")));
        }

        if (configuration.getBoolean("command-placeholder.enable")) {
            this.chatDisplays.add(new CommandDisplay(configuration.getString("command-placeholder.result"), configuration.getString("command-placeholder.permission")));
        }
    }

    @EventHandler
    public void onJoin(UserJoinEvent event) {
        User user = event.getUser();
        user.getDynamicCooldown().setSamples(this.chatCooldownMax);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTalk(AsyncChatEvent event) {

        Player player = event.getPlayer();
        User user = plugin.getUser(player.getUniqueId());

        if (user == null) {
            cancelEvent(event, Message.CHAT_ERROR);
            return;
        }

        if (!ConfigStorage.chatEnable && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_DISABLE)) {
            cancelEvent(event, Message.CHAT_DISABLE);
            return;
        }

        String message = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());
        final String minecraftMessage = message;

        ChatResult chatResult = analyzeMessage(user, message);
        if (!chatResult.isValid()) {
            cancelEvent(event, chatResult.message(), chatResult.arguments());
            return;
        }

        user.setLastMessage(message);

        if (!this.enableChatFormat) return;

        if (this.enableLinkTransform && hasPermission(player, Permission.ESSENTIALS_CHAT_LINK)) {
            message = transformUrlsToMiniMessage(message);
        }

        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        String chatFormat = papi(getChatFormat(player), player);

        String finalMessage = message;
        event.renderer((source, sourceDisplayName, ignoredMessage, viewer) -> {

            String localMessage = finalMessage;
            TagResolver.Builder builder = TagResolver.builder();

            boolean isModerator = viewer instanceof Player playerViewer && hasPermission(playerViewer, Permission.ESSENTIALS_CHAT_MODERATOR);
            if (viewer instanceof Player playerViewer) {
                for (ChatDisplay chatDisplay : this.chatDisplays) {
                    localMessage = chatDisplay.display(paperComponent, builder, player, playerViewer, localMessage);
                }
            }

            Tag tag = Tag.inserting(paperComponent.translateText(player, localMessage, builder.build()));
            return paperComponent.getComponentMessage(chatFormat, TagResolver.resolver("message", tag), "%displayName%", player.getDisplayName(), "%player%", player.getName(), "%moderator_action%", isModerator ? papi(getMessage(this.moderatorAction, "%player%", player.getName()), (Player) viewer) : "");
        });

        if (this.enableChatMessages) {
            this.plugin.getStorageManager().getStorage().insertChatMessage(player.getUniqueId(), minecraftMessage);
            this.chatMessagesCache.clear(player.getUniqueId());
        }
    }

    public ChatResult analyzeMessage(User user, String message) {
        Player player = user.getPlayer();

        if (this.enableAlphanumericRegex && !this.alphanumericPattern.matcher(message).find() && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_ALPHANUMERIC)) {
            return new ChatResult(false, Message.CHAT_ALPHANUMERIC_REGEX);
        }

        if (this.enableLinkRegex && this.linkPattern.matcher(message.replace(" ", "")).find() && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_LINK)) {
            return new ChatResult(false, Message.CHAT_LINK);
        }

        double cooldown = handleCooldown(user);
        if (this.enableChatDynamicCooldown && cooldown > 0 && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_DYNAMIC_COOLDOWN)) {
            return new ChatResult(false, Message.CHAT_COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(cooldown));
        }

        String lastMessage = user.getLastMessage();
        if (this.enableSameMessageCancel && message.equalsIgnoreCase(lastMessage) && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_SAME_MESSAGE)) {
            return new ChatResult(false, Message.CHAT_SAME);
        }

        return new ChatResult(true, null);
    }

    private String getChatFormat(Player player) {
        return this.chatFormats.stream().filter(chatFormat -> player.hasPermission(chatFormat.permission())).sorted(Comparator.comparingInt(ChatFormat::priority).reversed()).map(ChatFormat::format).findFirst().orElse(this.defaultChatFormat);
    }

    private double handleCooldown(User user) {

        long wait;

        DynamicCooldown dynamicCooldown = user.getDynamicCooldown();

        wait = dynamicCooldown.limited(user.getUniqueId(), this.chatCooldownArray);
        if (wait == 0L) dynamicCooldown.add(user.getUniqueId());

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

    public void sendChatHistory(CommandSender sender, UUID targetUuid, String targetName, int targetPage) {
        this.plugin.getScheduler().runAsync(wrappedTask -> {

            List<ChatMessageDTO> messages = this.chatMessagesCache.get(targetUuid, () -> this.plugin.getStorageManager().getStorage().getMessages(targetUuid));
            if (messages.isEmpty()) {
                message(sender, Message.CHAT_MESSAGES_EMPTY, "%player%", targetName);
                return;
            }

            Pagination<ChatMessageDTO> pagination = new Pagination<>();
            int maxPage = getMaxPage(messages, 10);
            int page = targetPage > maxPage ? maxPage : targetPage < 0 ? 1 : targetPage;

            pagination.paginate(messages, 10, page).forEach(chatMessageDTO -> message(sender, Message.CHAT_MESSAGES_LINE, "%date%", this.simpleDateFormat.format(chatMessageDTO.created_at()), "%message%", chatMessageDTO.content()));

            message(sender, Message.CHAT_MESSAGES_FOOTER, "%page%", page, "%nextPage%", page + 1, "%previousPage%", page - 1, "%maxPage%", maxPage, "%player%", targetName);
        });
    }
}
