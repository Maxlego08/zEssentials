package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
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
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MailBoxModule extends ZModule {

    private long expiration;

    public MailBoxModule(ZEssentialsPlugin plugin) {
        super(plugin, "mailbox");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("mailbox");
        this.loadInventory("mailbox_admin");
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

        message(sender, Message.MAILBOX_GIVE, "%item%", itemName, "%player%", username, "%amount%", realAmount);
    }

    public void giveAllItem(CommandSender sender, String itemName, int amount) {

        var itemModule = plugin.getModuleManager().getModule(ItemModule.class);
        if (!itemModule.isItem(itemName)) {
            message(sender, Message.MAILBOX_GIVE_ERROR, "%item%", itemName);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            var itemStack = itemModule.getItemStack(itemName, player);
            if (itemStack == null) break;

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

        message(sender, Message.MAILBOX_GIVE_ALL, "%item%", itemName, "%amount%", amount);
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

    private String getItemName(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            var itemMeta = itemStack.getItemMeta();
            if (itemMeta != null && itemMeta.hasDisplayName()) {
                return itemMeta.getDisplayName();
            }
        }
        return name(itemStack.getType().name());
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
