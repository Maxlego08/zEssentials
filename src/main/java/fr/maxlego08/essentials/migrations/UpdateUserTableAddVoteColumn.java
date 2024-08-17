package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddVoteColumn extends Migration {
    @Override
    public void up() {
        if (MigrationManager.getDatabaseConfiguration().getDatabaseType() == DatabaseType.SQLITE) {
            // Forced to make the query one by one otherwise SQLITE will not appreciate
            SchemaBuilder.alter(this, "%prefix%users", schema -> schema.bigInt("vote").defaultValue(0));
            SchemaBuilder.alter(this, "%prefix%users", schema -> schema.integer("vote_offline").defaultValue(0));
        } else {
            SchemaBuilder.alter(this, "%prefix%users", schema -> {
                schema.bigInt("vote").defaultValue(0);
                schema.integer("vote_offline").defaultValue(0);
            });
        }
    }
}
