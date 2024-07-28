package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateVoteSiteMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%vote_sites", table -> {
            table.uuid("player_id").primary();
            table.string("site", 255).primary();
            table.timestamp("last_vote_at");
        });
    }
}
