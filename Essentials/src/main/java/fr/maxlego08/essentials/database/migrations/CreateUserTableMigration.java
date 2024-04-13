package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateUserTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create("%prefix%players", table -> {
            table.uuid("unique_id").primary();
            table.string("name", 16);
            table.text("last_location").nullable();
            table.timestamps();
        });
    }
}
