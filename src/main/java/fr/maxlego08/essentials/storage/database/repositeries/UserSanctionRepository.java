package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class UserSanctionRepository extends Repository {
    public UserSanctionRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "sanctions");
    }

    public void insert(Sanction sanction, Consumer<Integer> consumer) {
        insert(table -> {
            table.uuid("player_unique_id", sanction.getPlayerUniqueId());
            table.uuid("sender_unique_id", sanction.getSenderUniqueId());
            table.string("sanction_type", sanction.getSanctionType().name());
            table.string("reason", sanction.getReason());
            table.decimal("duration", sanction.getDuration());
            table.date("expired_at", sanction.getExpiredAt());
        }, consumer);
    }

    public SanctionDTO getSanction(Integer integer) {
        List<SanctionDTO> sanctionDTOS = select(SanctionDTO.class, table -> table.where("id", integer));
        return sanctionDTOS.isEmpty() ? null : sanctionDTOS.getFirst();
    }

    public List<SanctionDTO> getActiveBan() {
        return select(SanctionDTO.class, table -> {
            table.leftJoin("%prefix%users", "zp", "ban_sanction_id", "%prefix%sanctions", "id");
            table.whereNotNull("zp.ban_sanction_id");
        });
    }

    public List<SanctionDTO> getSanctions(UUID uuid) {
        return select(SanctionDTO.class, table -> table.where("player_unique_id", uuid));
    }
}
