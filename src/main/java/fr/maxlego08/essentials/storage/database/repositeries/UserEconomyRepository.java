package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.UserEconomyDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class UserEconomyRepository extends Repository {

    public UserEconomyRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "economies");
    }

    public void upsert(UUID uuid, Economy economy, BigDecimal bigDecimal) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.string("economy_name", economy.getName()).primary();
            table.decimal("amount", bigDecimal);
        });
    }

    public void reset(Economy economy, BigDecimal amount) {
        update(table -> {
            table.decimal("amount", amount);
            table.where("economy_name", economy.getName());
        });
    }

    public List<EconomyDTO> select(UUID uuid) {
        return select(EconomyDTO.class, table -> table.where("unique_id", uuid));
    }

    public List<UserEconomyDTO> getAll() {
        return select(UserEconomyDTO.class, table -> {
        });
    }
}
