package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.PowerToolsDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

public class UserPowerToolsRepository extends Repository {
    public UserPowerToolsRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_power_tools");
    }

    public void upsert(UUID uuid, Material material, String command) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.string("material", material.name());
            table.string("command", command);
        });
    }

    public List<PowerToolsDTO> select(UUID uuid) {
        return this.select(PowerToolsDTO.class, table -> table.where("unique_id", uuid));
    }

    public void delete(UUID uuid, Material material) {
        this.delete(table -> table.where("unique_id", uuid).where("material", material.name()));
    }
}
