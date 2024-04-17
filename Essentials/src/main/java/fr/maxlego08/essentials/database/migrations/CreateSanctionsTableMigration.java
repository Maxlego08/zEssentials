package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateSanctionsTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%sanctions", table -> {
            table.autoIncrement("id").primary();
            table.uuid("player_unique_id").foreignKey("%prefix%users", "unique_id", true);
            table.uuid("sender_unique_id");
            table.string("sanction_type", 255);
            table.longText("reason");
            table.bigInt("duration");
            table.timestamp("expired_at").nullable();
            table.timestamps();
        });
    }
}
