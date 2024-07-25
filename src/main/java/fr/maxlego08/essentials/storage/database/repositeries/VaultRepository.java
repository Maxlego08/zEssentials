package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.VaultDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;

public class VaultRepository extends Repository {
    public VaultRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "vaults");
    }

    public List<VaultDTO> select() {
        return select(VaultDTO.class, table -> {
        });
    }
}
