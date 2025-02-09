package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.cache.ExpiringCache;
import fr.maxlego08.essentials.api.chat.ChatCooldown;
import fr.maxlego08.essentials.api.chat.ChatDisplay;
import fr.maxlego08.essentials.api.chat.ChatFormat;
import fr.maxlego08.essentials.api.chat.ChatPlaceholder;
import fr.maxlego08.essentials.api.chat.ChatResult;
import fr.maxlego08.essentials.api.chat.CustomRules;
import fr.maxlego08.essentials.api.chat.ShowItem;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.ChatMessageDTO;
import fr.maxlego08.essentials.api.event.events.user.UserJoinEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.messages.MessageUtils;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.DynamicCooldown;
import fr.maxlego08.essentials.chat.CommandDisplay;
import fr.maxlego08.essentials.chat.CustomDisplay;
import fr.maxlego08.essentials.chat.ItemDisplay;
import fr.maxlego08.essentials.chat.PlayerPingDisplay;
import fr.maxlego08.essentials.chat.ShowItemInventory;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ChatModule extends ZModule {

    private final List<ShowItem> showItems = new ArrayList<>();
    private final List<ChatDisplay> chatDisplays = new ArrayList<>();
    private final ExpiringCache<UUID, List<ChatMessageDTO>> chatMessagesCache = new ExpiringCache<>(1000 * 60);
    private final Pattern urlPattern = Pattern.compile("(https?://[\\w-\\.]+(\\:[0-9]+)?(/[\\w- ./?%&=]*)?)", Pattern.CASE_INSENSITIVE);
    private final List<ChatCooldown> chatCooldowns = new ArrayList<>();
    private final List<ChatFormat> chatFormats = new ArrayList<>();
    private final List<ChatPlaceholder> chatPlaceholders = new ArrayList<>();
    private final List<CustomRules> customRules = new ArrayList<>();
    private ChatDisplay pingDisplay;
    private String alphanumericRegex;
    private String linkRegex;
    private String itemaddersFontRegex;
    private String defaultChatFormat;
    private String moderatorAction;
    private String linkTransform;
    private String dateFormat;
    private String antiFloodRegex;
    private SimpleDateFormat simpleDateFormat;
    private Pattern playerNamePattern;
    private Pattern alphanumericPattern;
    private Pattern linkPattern;
    private Pattern fontPattern;
    private Pattern floodRegex;
    private boolean enableAlphanumericRegex;
    private boolean enableLinkRegex;
    private boolean enableItemaddersFontRegex;
    private boolean enableChatDynamicCooldown;
    private boolean enableSameMessageCancel;
    private boolean enableChatFormat;
    private boolean enablePing;
    private boolean enableAntiFlood;
    private boolean enableLinkTransform;
    private boolean enableChatMessages;
    private int chatCooldownMax;
    private boolean enableCaps;
    private double capsThreshold;
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

        this.alphanumericPattern = Pattern.compile(or(this.alphanumericRegex, "^[a-zA-Z0-9_.?!^¨%ù*&é\"#'{(\\[-|èêë`\\\\çà)\\]=}ûî+<>:²€$/\\-,-â@;ô ]+$"));
        this.linkPattern = Pattern.compile(or(this.linkRegex, "[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)"));
        this.fontPattern = Pattern.compile(or(this.itemaddersFontRegex, "(?<=:)(.*?)(?=\\s*\\:)"));
        this.floodRegex = Pattern.compile(or(this.antiFloodRegex, "(.)\\1{3,}"));
        this.chatCooldownArray = this.chatCooldowns.stream().flatMapToLong(cooldown -> LongStream.of(cooldown.cooldown(), cooldown.messages())).toArray();
        this.simpleDateFormat = new SimpleDateFormat(or(this.dateFormat, "yyyy-MM-dd HH:mm:ss"));

        this.chatDisplays.clear();
        if (this.enablePing) {
            this.pingDisplay = new PlayerPingDisplay(this.playerPingColor, this.playerPingColorOther, this.playerPingSound, this.playerPingSoundVolume, this.playerPingSoundPitch);
        }

        Pattern pattern = Pattern.compile("[!?#]?[a-z0-9_-]*");
        this.chatPlaceholders.forEach(chatPlaceholder -> {
            Matcher matcher = pattern.matcher(chatPlaceholder.name());
            if (matcher.find()) {
                this.chatDisplays.add(new CustomDisplay(chatPlaceholder.name(), chatPlaceholder.regex(), chatPlaceholder.result(), chatPlaceholder.permission()));
            } else {
                plugin.getLogger().severe("Custom Placeholders name must match pattern [!?#]?[a-z0-9_-]*, was " + chatPlaceholder.name() + ", Possible correction: " + MessageUtils.removeNonAlphanumeric(chatPlaceholder.name()));
            }
        });

        YamlConfiguration configuration = getConfiguration();
        if (configuration.getBoolean("item-placeholder.enable")) {
            this.chatDisplays.add(new ItemDisplay(this.plugin, configuration.getString("item-placeholder.regex"), configuration.getString("item-placeholder.result"), configuration.getString("item-placeholder.permission")));
        }

        if (configuration.getBoolean("command-placeholder.enable")) {
            this.chatDisplays.add(new CommandDisplay(configuration.getString("command-placeholder.result"), configuration.getString("command-placeholder.permission")));
        }

        this.customRules.removeIf(CustomRules::isNotValid);
    }

    @EventHandler
    public void onJoin(UserJoinEvent event) {
        User user = event.getUser();
        user.getDynamicCooldown().setSamples(this.chatCooldownMax);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTalk(AsyncChatEvent event) {

        if (event.isCancelled()) return;

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

        Optional<CustomRules> optional = this.customRules.stream().filter(rule -> rule.match(player, minecraftMessage)).findFirst();
        if (optional.isPresent()) {
            message(player, optional.get().message());
            event.setCancelled(true);
            return;
        }

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

        TagResolver.Builder builder = TagResolver.builder();
        for (ChatDisplay chatDisplay : this.chatDisplays) {
            message = chatDisplay.display(paperComponent, builder, player, null, message);
        }

        String finalMessage = message;
        event.renderer((source, sourceDisplayName, ignoredMessage, viewer) -> {

            String localMessage = finalMessage;

            boolean isModerator = viewer instanceof Player playerViewer && hasPermission(playerViewer, Permission.ESSENTIALS_CHAT_MODERATOR);
            if (viewer instanceof Player playerViewer) {
                if (this.pingDisplay != null) {
                    localMessage = this.pingDisplay.display(paperComponent, builder, player, playerViewer, localMessage);
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

        if (message.length() > 3 && this.enableCaps && containsTooManyCaps(message) && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_CAPS)) {
            return new ChatResult(false, Message.CHAT_CAPS);
        }

        if (this.enableAntiFlood && containsFlood(message) && !hasPermission(player, Permission.ESSENTIALS_CHAT_BYPASS_FLOOD)) {
            return new ChatResult(false, Message.CHAT_FLOOD);
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

            pagination.paginate(messages, 10, page).forEach(chatMessageDTO -> message(sender, Message.CHAT_MESSAGES_LINE, "%date%", format(chatMessageDTO.created_at()), "%message%", chatMessageDTO.content()));

            message(sender, Message.CHAT_MESSAGES_FOOTER, "%page%", page, "%nextPage%", page + 1, "%previousPage%", page - 1, "%maxPage%", maxPage, "%player%", targetName);
        });
    }


    private String format(Date date){
        if (date == null) return "";
        return this.simpleDateFormat.format(date);
    }

    private void updatePlayerNamePattern() {
        String patternString = Bukkit.getOnlinePlayers().stream().map(Player::getName).map(Pattern::quote).collect(Collectors.joining("|", "\\b(", ")\\b"));
        this.playerNamePattern = Pattern.compile(patternString);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        updatePlayerNamePattern();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerNamePattern();
    }

    private boolean containsTooManyCaps(String message) {
        if (this.playerNamePattern != null) {
            Matcher matcher = this.playerNamePattern.matcher(message);
            message = matcher.replaceAll("");  // Remove nicknames from players
        }

        int upperCaseCount = 0;
        int totalLetterCount = 0;

        // Count the number of capital letters and total letters
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                totalLetterCount++;
                if (Character.isUpperCase(c)) {
                    upperCaseCount++;
                }
            }
        }

        // If the message does not contain letters, it cannot be considered capitalized spam
        if (totalLetterCount == 0) {
            return false;
        }

        // Calculate percentage of caps
        double upperCasePercentage = (double) upperCaseCount / totalLetterCount;

        // Check if the percentage of caps exceeds the threshold
        return upperCasePercentage > capsThreshold;
    }


    public boolean containsFlood(String message) {
        return this.floodRegex.matcher(message).find();
    }

    public String createHoverItemStack(Player player, ItemStack itemStack) {

        this.showItems.removeIf(ShowItem::isExpired);

        String code = generateRandomString(16);

        ShowItem showItem = new ShowItem(player, itemStack, System.currentTimeMillis() + (1000 * 300), code);
        this.showItems.add(showItem);

        return code;
    }

    public void openShowItem(Player player, String code) {

        this.showItems.removeIf(ShowItem::isExpired);
        Optional<ShowItem> optional = this.showItems.stream().filter(showItem -> showItem.code().equals(code)).findFirst();
        if (optional.isEmpty()) {
            message(player, Message.CODE_NOT_FOUND);
            return;
        }

        new ShowItemInventory(optional.get(), player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof ShowItemInventory) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ShowItemInventory) {
            event.setCancelled(true);
        }
    }

}
