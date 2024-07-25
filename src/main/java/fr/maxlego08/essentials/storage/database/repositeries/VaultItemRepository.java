package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VaultItemRepository extends Repository {

    private final ConcurrentHashMap<CacheKey, CacheValue> caches = new ConcurrentHashMap<>();

    public VaultItemRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "vault_items");
    }

    public List<VaultItemDTO> select() {
        return select(VaultItemDTO.class, table -> {
        });
    }

    public void updateQuantity(UUID uniqueId, int vaultId, int slot, long quantity) {

        var key = new CacheKey(uniqueId, vaultId, slot);
        if (this.caches.containsKey(key)) {
            this.caches.get(key).add(quantity);
            return;
        }

        this.caches.put(key, new CacheValue(System.currentTimeMillis(), quantity));
        this.startTask(key);
    }

    private void startTask(CacheKey key) {
        this.plugin.getScheduler().runLaterAsync(() -> {

            long currentTime = System.currentTimeMillis();
            if (this.caches.containsKey(key)) {
                var value = this.caches.get(key);
                if (currentTime - value.getCreatedAt() >= 200) {
                    this.caches.remove(key);
                    this.update(table -> {
                        table.bigInt("quantity", value.quantity);
                        table.where("unique_id", key.uniqueId);
                        table.where("vault_id", key.vaultId);
                        table.where("slot", key.slot);
                    });
                } else {

                    this.startTask(key);
                }
            }
        }, 4);
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
