package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.function.Consumer;

public class UserSanctionRepository extends Repository {
    public UserSanctionRepository(SqlConnection connection) {
        super(connection, "sanctions");
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
}
