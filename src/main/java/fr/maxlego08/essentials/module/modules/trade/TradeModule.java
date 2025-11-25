package fr.maxlego08.essentials.module.modules.trade;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.module.ZModule;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import fr.maxlego08.essentials.api.cache.SimpleCache;

public class TradeModule extends ZModule {

    private TradeManager tradeManager;

    private long requestTimeout;
    private double maxDistance;
    private List<String> worlds;
    private List<Integer> ownSlots = new ArrayList<>();
    private List<Integer> partnerSlots = new ArrayList<>();
    private final SimpleCache<String, ItemStack> itemCache = new SimpleCache<>();

    public TradeModule(ZEssentialsPlugin plugin) {
        super(plugin, "trade");
        this.tradeManager = new TradeManager(plugin, this);
        org.bukkit.Bukkit.getPluginManager().registerEvents(new fr.maxlego08.essentials.module.modules.trade.listeners.TradeInventoryListener(this), plugin);
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();
        var config = getConfiguration();
        this.requestTimeout = config.getLong("request-timeout", 60);
        this.maxDistance = config.getDouble("max-distance", 10.0);
        this.worlds = config.getStringList("worlds");
        
        this.ownSlots = parseSlots(config.getStringList("own-slots"));
        this.partnerSlots = parseSlots(config.getStringList("partner-slots"));
        this.itemCache.clear();
    }
    
    private List<Integer> parseSlots(List<String> slotStrings) {
        List<Integer> slots = new ArrayList<>();
        for (String s : slotStrings) {
            try {
                if (s.contains("-")) {
                    String[] parts = s.split("-");
                    int start = Integer.parseInt(parts[0]);
                    int end = Integer.parseInt(parts[1]);
                    for (int i = start; i <= end; i++) slots.add(i);
                } else {
                    slots.add(Integer.parseInt(s));
                }
            } catch (NumberFormatException ignored) {}
        }
        return slots;
    }
    
    public List<Integer> getOwnSlots() {
        return ownSlots;
    }
    
    public List<Integer> getPartnerSlots() {
        return partnerSlots;
    }
    
    public void sendMessage(Player player, String key, String... replacements) {
        String message = getConfiguration().getString("messages." + key);
        if (message == null) return;

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        this.plugin.getComponentMessage().sendMessage(player, message);
    }
    
    public ItemStack getItem(String path, Player player, String... replacements) {
        if (replacements.length == 0) {
             ItemStack cached = itemCache.get(path, () -> loadRawItem(path));
             if (cached != null) {
                 ItemStack clone = cached.clone();
                 updateItemMeta(clone, player);
                 return clone;
             }
        }
        
        ItemStack item = loadRawItem(path);
        updateItemMeta(item, player, replacements);
        return item;
    }
    
    private ItemStack loadRawItem(String path) {
        var config = getConfiguration();
        var section = config.getConfigurationSection(path);
        if (section == null) return new ItemStack(org.bukkit.Material.AIR);
        
        String materialName = section.getString("material", "STONE");
        org.bukkit.Material material = org.bukkit.Material.matchMaterial(materialName);
        if (material == null) material = org.bukkit.Material.STONE;
        
        ItemStack item = new ItemStack(material);
        var meta = item.getItemMeta();
        if (meta != null) {
            if (section.contains("custom-model-data")) {
                meta.setCustomModelData(section.getInt("custom-model-data"));
            }
            
            String name = section.getString("name");
            if (name != null) {
                meta.setDisplayName(name);
            }
            
            List<String> lore = section.getStringList("lore");
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }
            
            item.setItemMeta(meta);
        }
        return item;
    }
    
    private void updateItemMeta(ItemStack item, Player player, String... replacements) {
        var meta = item.getItemMeta();
        if (meta == null) return;
        
        if (meta.hasDisplayName()) {
            String name = meta.getDisplayName();
            for (int i = 0; i < replacements.length; i += 2) {
                if (i + 1 < replacements.length) name = name.replace(replacements[i], replacements[i + 1]);
            }
            if (this.plugin.getComponentMessage() instanceof fr.maxlego08.essentials.zutils.utils.paper.PaperComponent paperComponent) {
                paperComponent.updateDisplayName(meta, name, player);
            }
        }
        
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore != null && !lore.isEmpty()) {
                List<String> newLore = new ArrayList<>();
                for (String line : lore) {
                    for (int i = 0; i < replacements.length; i += 2) {
                        if (i + 1 < replacements.length) line = line.replace(replacements[i], replacements[i + 1]);
                    }
                    newLore.add(line);
                }
                if (this.plugin.getComponentMessage() instanceof fr.maxlego08.essentials.zutils.utils.paper.PaperComponent paperComponent) {
                    paperComponent.updateLore(meta, newLore, player);
                }
            }
        }
        item.setItemMeta(meta);
    }
    
    public int getOwnConfirmSlot() {
        return getConfiguration().getInt("own.confirm-item.slot", 0);
    }
    
    public int getPartnerConfirmSlot() {
        return getConfiguration().getInt("partner.confirm-item.slot", 8);
    }
    
    public long getRequestTimeout() {
        return requestTimeout;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public List<String> getWorlds() {
        return worlds;
    }
    
    public TradeManager getTradeManager() {
        return tradeManager;
    }
    
    public void onDisable() {
        if (this.tradeManager != null) {
            this.tradeManager.cancelAllTrades();
        }
    }
}

