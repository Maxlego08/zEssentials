package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.mailbox.MailMessage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MailBoxModule extends ZModule {

    // Do not make those fields final, javac would inline the constant and the configuration would be ignored
    private long expiration;
    private boolean messageNotifyOnJoin = true;
    private long messageNotifyDelay = 3;
    private int messageMaxAmount = 50;
    private int messageMaxLength = 256;
    private long messageCooldown = 5;
    private String messageDateFormat = "yyyy-MM-dd HH:mm";

    @NonLoadable
    private SimpleDateFormat simpleDateFormat;

    public MailBoxModule(ZEssentialsPlugin plugin) {
        super(plugin, "mailbox");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("mailbox");
        this.loadInventory("mailbox_admin");

        this.simpleDateFormat = new SimpleDateFormat(this.messageDateFormat);
    }

    /**
     * Sends a text message to a player, online or not. The message is stored and can be read with /mail read.
     *
     * @param sender       the sender of the message, a player or the console
     * @param receiverId   the UUID of the receiver
     * @param receiverName the name of the receiver
     * @param content      the message
     */
    public void sendMailMessage(CommandSender sender, UUID receiverId, String receiverName, String content) {

        if (content.length() > this.messageMaxLength) {
            message(sender, Message.MAILBOX_MESSAGE_TOO_LONG, "%max%", this.messageMaxLength);
            return;
        }

        UUID senderId = sender instanceof Player player ? player.getUniqueId() : this.plugin.getConsoleUniqueId();
        String senderName = sender instanceof Player player ? player.getName() : getMessage(Message.CONSOLE);

        User senderUser = sender instanceof Player player ? this.plugin.getUser(player.getUniqueId()) : null;
        if (senderUser != null) {

            if (senderUser.getUniqueId().equals(receiverId)) {
                message(sender, Message.MAILBOX_MESSAGE_SELF);
                return;
            }

            if (senderUser.isMute()) {
                message(sender, Message.MAILBOX_MESSAGE_MUTE);
                return;
            }

            if (!checkCooldown(senderUser)) return;
        }

        User receiver = this.plugin.getUser(receiverId);
        if (receiver != null) {

            if (receiver.isIgnore(senderId)) {
                message(sender, Message.MAILBOX_MESSAGE_IGNORE, "%player%", receiverName);
                return;
            }

            if (receiver.getMailMessages().size() >= this.messageMaxAmount) {
                message(sender, Message.MAILBOX_MESSAGE_FULL, "%player%", receiverName);
                return;
            }

            receiver.addMailMessage(new MailMessage(receiverId, senderId, senderName, content, new Date()));
            message(receiver, Message.MAILBOX_MESSAGE_RECEIVE, "%player%", senderName, "%message%", content);
            message(sender, Message.MAILBOX_MESSAGE_SEND, "%player%", receiverName, "%message%", content);
            return;
        }

        // The receiver is offline, the amount of stored messages must be read from the storage
        this.plugin.getScheduler().runAsync(wrappedTask -> {

            IStorage iStorage = getStorage();
            if (iStorage.getMailMessages(receiverId).size() >= this.messageMaxAmount) {
                message(sender, Message.MAILBOX_MESSAGE_FULL, "%player%", receiverName);
                return;
            }

            iStorage.addMailMessage(new MailMessage(receiverId, senderId, senderName, content, new Date()));
            message(sender, Message.MAILBOX_MESSAGE_SEND, "%player%", receiverName, "%message%", content);
        });
    }

    /**
     * Sends a text message to every online player.
     *
     * @param sender  the sender of the message
     * @param content the message
     */
    public void sendMailMessageToAll(CommandSender sender, String content) {

        if (content.length() > this.messageMaxLength) {
            message(sender, Message.MAILBOX_MESSAGE_TOO_LONG, "%max%", this.messageMaxLength);
            return;
        }

        UUID senderId = sender instanceof Player player ? player.getUniqueId() : this.plugin.getConsoleUniqueId();
        String senderName = sender instanceof Player player ? player.getName() : getMessage(Message.CONSOLE);

        int amount = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {

            User receiver = this.plugin.getUser(player.getUniqueId());
            if (receiver == null || receiver.getUniqueId().equals(senderId)) continue;
            if (receiver.getMailMessages().size() >= this.messageMaxAmount) continue;

            receiver.addMailMessage(new MailMessage(receiver.getUniqueId(), senderId, senderName, content, new Date()));
            message(receiver, Message.MAILBOX_MESSAGE_RECEIVE, "%player%", senderName, "%message%", content);
            amount++;
        }

        message(sender, Message.MAILBOX_MESSAGE_SEND_ALL, "%amount%", amount, "%message%", content);
    }

    /**
     * Displays the text messages of a user and marks them as read.
     *
     * @param user the user reading his messages
     */
    public void readMailMessages(User user) {

        List<MailMessage> mailMessages = user.getMailMessages();
        if (mailMessages.isEmpty()) {
            message(user, Message.MAILBOX_MESSAGE_EMPTY);
            return;
        }

        message(user, Message.MAILBOX_MESSAGE_HEADER, "%amount%", mailMessages.size());
        for (MailMessage mailMessage : mailMessages) {
            message(user, Message.MAILBOX_MESSAGE_LINE, "%player%", mailMessage.getSenderName(), "%date%", mailMessage.getCreatedAt() == null ? "" : this.simpleDateFormat.format(mailMessage.getCreatedAt()), "%message%", mailMessage.getContent());
        }
        message(user, Message.MAILBOX_MESSAGE_FOOTER);

        if (user.countUnreadMailMessages() > 0) {
            mailMessages.forEach(mailMessage -> mailMessage.setRead(true));
            getStorage().markMailMessagesAsRead(user.getUniqueId());
        }
    }

    /**
     * Deletes every text message of a user.
     *
     * @param sender   the sender of the command
     * @param uuid     the UUID of the user
     * @param username the name of the user
     */
    public void clearMailMessages(CommandSender sender, UUID uuid, String username) {

        // The list must be cleared before the storage call, the JSON storage saves the user
        // asynchronously and would write the messages back
        User user = getUser(uuid);
        if (user != null) {
            user.getMailMessages().clear();
        }

        getStorage().clearMailMessages(uuid);

        message(sender, Message.MAILBOX_MESSAGE_CLEAR, "%player%", username);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (!this.messageNotifyOnJoin) return;

        Player player = event.getPlayer();
        this.plugin.getScheduler().runAtLocationLater(player.getLocation(), wrappedTask -> {

            User user = this.plugin.getUser(player.getUniqueId());
            if (user == null) return;

            long unread = user.countUnreadMailMessages();
            if (unread <= 0) return;

            message(user, Message.MAILBOX_MESSAGE_NOTIFY, "%amount%", unread);
        }, Math.max(1, this.messageNotifyDelay), TimeUnit.SECONDS);
    }

    /**
     * Checks and registers the cooldown between two messages.
     *
     * @param user the sender
     * @return true if the message can be sent
     */
    private boolean checkCooldown(User user) {

        if (this.messageCooldown <= 0) return true;
        if (user.hasPermission(Permission.ESSENTIALS_BYPASS_COOLDOWN) && this.plugin.getConfiguration().enableCooldownBypass()) {
            return true;
        }

        String key = "mail-message-send";
        if (user.isCooldown(key)) {
            message(user, Message.COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(user.getCooldown(key) - System.currentTimeMillis()));
            return false;
        }

        user.addCooldown(key, this.messageCooldown);
        return true;
    }

    public void addItemAndFix(UUID uuid, ItemStack itemStack) {
        int amount = itemStack.getAmount();
        if (amount > itemStack.getMaxStackSize()) {
            while (amount > 0) {
                int currentAmount = Math.min(itemStack.getMaxStackSize(), amount);
                amount -= currentAmount;

                ItemStack clonedItemStacks = itemStack.clone();
                clonedItemStacks.setAmount(currentAmount);

                addItem(uuid, clonedItemStacks);
            }
        } else {
            addItem(uuid, itemStack);
        }
    }

    public void addItem(UUID uuid, ItemStack itemStack) {

        MailBoxItem mailBoxItem = new MailBoxItem(uuid, itemStack, new Date(System.currentTimeMillis() + (this.expiration * 1000)));

        User user = this.plugin.getUser(uuid);
        if (user != null) {

            user.addMailBoxItem(mailBoxItem);
            message(user, Message.MAILBOX_ADD);
        } else {

            IStorage iStorage = this.plugin.getStorageManager().getStorage();
            iStorage.addMailBoxItem(mailBoxItem);
        }
    }

    public void openMailBox(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "mailbox");
    }

    public void openMailBoxAdmin(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "mailbox_admin");
    }

    public void removeItem(User user, Player player, MailBoxItem mailBoxItem) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        PlayerInventory inventory = player.getInventory();

        int firstEmptySlot = inventory.firstEmpty();
        if (firstEmptySlot == -1) {
            message(player, Message.MAILBOX_REMOVE_FULL);
            return;
        }

        List<MailBoxItem> mailBoxItems = user.getMailBoxItems();

        if (mailBoxItem.isExpired()) {
            message(player, Message.MAILBOX_REMOVE_EXPIRE);
            openMailBox(player);

            mailBoxItems.remove(mailBoxItem);
            iStorage.removeMailBoxItem(mailBoxItem.getId());
            return;
        }

        if (mailBoxItems.contains(mailBoxItem)) {

            inventory.addItem(mailBoxItem.getItemStack());

            mailBoxItems.remove(mailBoxItem);
            iStorage.removeMailBoxItem(mailBoxItem.getId());
        }

        if (user.getUniqueId().equals(player.getUniqueId())) {
            openMailBox(player);
        } else {
            openMailBoxAdmin(player);
        }
    }

    public void openMailBox(User user, UUID uuid, String username) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        List<MailBoxDTO> mailBoxDTOS = iStorage.getMailBox(uuid);
        User fakeUser = ZUser.fakeUser(this.plugin, uuid, username);
        fakeUser.setMailBoxItems(mailBoxDTOS);

        user.setTargetUser(fakeUser);
        openMailBoxAdmin(user.getPlayer());
    }

    public void giveItem(CommandSender sender, UUID uuid, String username, String itemName, int amount) {

        var itemModule = plugin.getModuleManager().getModule(ItemModule.class);
        if (itemModule == null) {
            message(sender, Message.MAILBOX_GIVE_ERROR, "%item%", itemName);
            return;
        }
        var itemStack = itemModule.getItemStack(itemName, Bukkit.getPlayer(uuid));

        if (itemStack == null) {
            message(sender, Message.MAILBOX_GIVE_ERROR, "%item%", itemName);
            return;
        }

        int realAmount = 0;
        if (amount > itemStack.getMaxStackSize()) {

            while (amount > 0) {

                ItemStack newItemStack = itemStack.clone();
                int currentAmount = Math.min(amount, itemStack.getMaxStackSize());
                if (currentAmount <= 0) break;

                amount -= currentAmount;
                realAmount += currentAmount;

                newItemStack.setAmount(currentAmount);
                addItem(uuid, newItemStack);
            }

        } else {
            int currentAmount = Math.max(1, amount);
            realAmount = currentAmount;
            itemStack.setAmount(currentAmount);
            addItem(uuid, itemStack);
        }

        message(sender, Message.MAILBOX_GIVE, "%item%", getItemName(itemStack), "%player%", username, "%amount%", realAmount);
    }

    public void giveAllItem(CommandSender sender, String itemName, int amount) {

        var itemModule = plugin.getModuleManager().getModule(ItemModule.class);
        if (itemModule == null || !itemModule.isItem(itemName)) {
            message(sender, Message.MAILBOX_GIVE_ERROR, "%item%", itemName);
            return;
        }

        String displayName = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            var itemStack = itemModule.getItemStack(itemName, player);
            if (itemStack == null) break;
            if (displayName == null) displayName = getItemName(itemStack);

            if (amount > itemStack.getMaxStackSize()) {

                int playerAmount = amount;
                while (playerAmount > 0) {

                    ItemStack newItemStack = itemStack.clone();
                    int currentAmount = Math.min(playerAmount, itemStack.getMaxStackSize());
                    if (currentAmount <= 0) break;

                    playerAmount -= currentAmount;

                    newItemStack.setAmount(currentAmount);
                    addItem(player.getUniqueId(), newItemStack);
                }

            } else {
                itemStack.setAmount(Math.max(1, amount));
                addItem(player.getUniqueId(), itemStack);
            }
        }

        message(sender, Message.MAILBOX_GIVE_ALL, "%item%", displayName == null ? itemName : displayName, "%amount%", amount);
    }

    public void giveItemFromHand(CommandSender sender, UUID uuid, String username, ItemStack itemStack) {

        if (itemStack == null || itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_ITEM_EMPTY);
            return;
        }

        ItemStack clonedItemStack = itemStack.clone();
        addItemAndFix(uuid, clonedItemStack);

        message(sender, Message.MAILBOX_GIVE_HAND,
                "%item%", getItemName(clonedItemStack),
                "%player%", username,
                "%amount%", clonedItemStack.getAmount());
    }

    public void giveAllItemFromHand(CommandSender sender, ItemStack itemStack) {

        if (itemStack == null || itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_ITEM_EMPTY);
            return;
        }

        String itemName = getItemName(itemStack);
        int amount = itemStack.getAmount();

        for (Player player : Bukkit.getOnlinePlayers()) {
            addItemAndFix(player.getUniqueId(), itemStack.clone());
        }

        message(sender, Message.MAILBOX_GIVE_ALL_HAND,
                "%item%", itemName,
                "%amount%", amount);
    }

    public void clear(CommandSender sender, UUID uuid, String username) {
        getStorage().clearMailBox(uuid);
        var user = getUser(uuid);
        if (user != null) {
            user.setMailBoxItems(List.of());
        }
        message(sender, Message.MAILBOX_CLEAR, "%player%", username);
    }
}
