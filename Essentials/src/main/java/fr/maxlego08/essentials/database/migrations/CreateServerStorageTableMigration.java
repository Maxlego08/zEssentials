package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateServerStorageTableMigration extends Migration {

    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%storages", table -> {
            table.string("name", 255).primary();
            table.longText("content");
            table.timestamps();
        });
    }
}
