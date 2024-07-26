package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.VaultDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class VaultRepository extends Repository {
    public VaultRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "vaults");
    }

    public List<VaultDTO> select() {
        return select(VaultDTO.class, table -> {
        });
    }

    public void update(UUID uniqueId, int vaultId, String name, String icon) {
        upsert(table -> {
            table.uuid("unique_id", uniqueId).primary();
            table.bigInt("vault_id", vaultId).primary();
            table.string("name", name);
            table.string("icon", icon);
        });
    }
}
