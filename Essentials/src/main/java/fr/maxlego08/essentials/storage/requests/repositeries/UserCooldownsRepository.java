package fr.maxlego08.essentials.storage.requests.repositeries;

import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.storage.requests.Repository;
import fr.maxlego08.essentials.storage.requests.SqlConnection;

import java.util.List;
import java.util.UUID;

public class UserCooldownsRepository extends Repository {

    public UserCooldownsRepository(SqlConnection connection) {
        super(connection, "player_cooldowns");
    }

    public void upsert(UUID uuid, String cooldownName, long cooldownValue) {
        upsert(table -> {
            table.uuid("unique_id", uuid);
            table.string("cooldown_name", cooldownName);
            table.bigInt("cooldown_value", cooldownValue);
        });
    }

    public List<CooldownDTO> selectCooldowns(UUID uuid) {
        return select(CooldownDTO.class, schema -> schema.where("unique_id", uuid.toString()));
    }

    public void deleteExpiredCooldowns() {
        delete(table -> table.where("cooldown_value", "<", System.currentTimeMillis()));
    }
}
