package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

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
