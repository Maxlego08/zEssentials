package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.VoteSiteDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class VoteSiteRepository extends Repository {
    public VoteSiteRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "vote_sites");
    }

    public List<VoteSiteDTO> select(UUID uniqueId) {
        return select(VoteSiteDTO.class, table -> table.where("player_id", uniqueId));
    }

    public void setLastVote(UUID uniqueId, String site) {
        upsert(table -> {
            table.uuid("player_id", uniqueId).primary();
            table.string("site", site);
            table.object("last_vote_at", new Date());
        });
    }
}
