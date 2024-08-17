package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddSanctionColumns extends Migration {

    @Override
    public void up() {
        if (MigrationManager.getDatabaseConfiguration().getDatabaseType() == DatabaseType.SQLITE) {
            // Forced to make the query one by one otherwise SQLITE will not appreciate
            SchemaBuilder.alter(this, "%prefix%users", schema -> schema.bigInt("ban_sanction_id").nullable());
            SchemaBuilder.alter(this, "%prefix%users", schema -> schema.bigInt("mute_sanction_id").nullable());
        } else {
            SchemaBuilder.alter(this, "%prefix%users", schema -> {
                schema.bigInt("ban_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
                schema.bigInt("mute_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
            });
        }
    }
}
