package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.PlayerSlotDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class PlayerSlotRepository extends Repository {

    public PlayerSlotRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "player_slots");
    }

    public List<PlayerSlotDTO> select() {
        return select(PlayerSlotDTO.class, table -> {
        });
    }

    public void setSlot(UUID uniqueId, int slot) {

        if (slot == 0) {
            this.deleteSlot(uniqueId);
            return;
        }

        upsert(table -> {
            table.uuid("unique_id", uniqueId).primary();
            table.bigInt("slots", slot);
        });
    }

    public void deleteSlot(UUID uniqueId) {
        delete(table -> table.where("unique_id", uniqueId));
    }
}
