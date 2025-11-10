package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VaultItemRepository extends Repository {

    private final ConcurrentHashMap<CacheKey, CacheValue> caches = new ConcurrentHashMap<>();
    // Single scheduled task for batch updates instead of creating new tasks for each update
    private volatile boolean batchTaskScheduled = false;

    public VaultItemRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "vault_items");
    }

    public List<VaultItemDTO> select() {
        return select(VaultItemDTO.class, table -> {
        });
    }

    public Optional<VaultItemDTO> select(UUID uniqueId, int vaultId, int slot) {
        return select(VaultItemDTO.class, table -> {
            table.where("unique_id", uniqueId);
            table.where("vault_id", vaultId);
            table.where("slot", slot);
        }).stream().findFirst();
    }

    public void updateQuantity(UUID uniqueId, int vaultId, int slot, long quantity) {

        var key = new CacheKey(uniqueId, vaultId, slot);
        this.caches.compute(key, (k, value) -> {
            if (value != null) {
                value.add(quantity);
                return value;
            }

            this.startTask(k);
            return new CacheValue(System.currentTimeMillis(), quantity);
        });
    }

    private void startTask(CacheKey key) {
        // Prevent creating multiple scheduled tasks - performance optimization
        if (!batchTaskScheduled) {
            batchTaskScheduled = true;
            
            this.plugin.getScheduler().runLaterAsync(() -> {
                // Process all pending cache updates at once
                processCacheUpdates();
                batchTaskScheduled = false;
            }, 4);
        }
    }
    
    private void processCacheUpdates() {
        long currentTime = System.currentTimeMillis();
        
        // Process all cached items in a single batch
        caches.entrySet().removeIf(entry -> {
            CacheKey key = entry.getKey();
            CacheValue value = entry.getValue();
            
            if (currentTime - value.getCreatedAt() >= 200) {
                // Update database
                this.update(table -> {
                    table.bigInt("quantity", value.quantity);
                    table.where("unique_id", key.uniqueId);
                    table.where("vault_id", key.vaultId);
                    table.where("slot", key.slot);
                });
                return true; // Remove from cache
            }
            return false; // Keep in cache
        });
        
        // Schedule next check if there are still items in cache
        if (!caches.isEmpty() && !batchTaskScheduled) {
            batchTaskScheduled = true;
            this.plugin.getScheduler().runLaterAsync(() -> {
                processCacheUpdates();
                batchTaskScheduled = false;
            }, 4);
        }
    }

    public void createNewItem(UUID uniqueId, int vaultId, int slot, long quantity, String item) {
        this.insert(table -> {
            table.uuid("unique_id", uniqueId);
            table.bigInt("vault_id", vaultId);
            table.bigInt("slot", slot);
            table.string("item", item);
            table.bigInt("quantity", quantity);
        });
    }

    public void removeItem(UUID uniqueId, int vaultId, int slot) {
        this.caches.remove(new CacheKey(uniqueId, vaultId, slot));
        this.delete(table -> {
            table.where("unique_id", uniqueId);
            table.where("vault_id", vaultId);
            table.where("slot", slot);
        });
    }

    public boolean forceRemove(UUID uniqueId, int vaultId, int slot) {
        this.caches.remove(new CacheKey(uniqueId, vaultId, slot));
        int result = this.delete(table -> {
            table.where("unique_id", uniqueId);
            table.where("vault_id", vaultId);
            table.where("slot", slot);
        });
        return result > 0;
    }

    public record CacheKey(UUID uniqueId, int vaultId, int slot) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return vaultId == cacheKey.vaultId && slot == cacheKey.slot && Objects.equals(uniqueId, cacheKey.uniqueId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uniqueId, vaultId, slot);
        }
    }

    public static class CacheValue {

        private long createdAt;
        private long quantity;

        public CacheValue(long createdAt, long quantity) {
            this.createdAt = createdAt;
            this.quantity = quantity;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public long getQuantity() {
            return quantity;
        }

        public void add(long quantity) {
            this.createdAt = System.currentTimeMillis();
            this.quantity = quantity;
        }
    }
}
