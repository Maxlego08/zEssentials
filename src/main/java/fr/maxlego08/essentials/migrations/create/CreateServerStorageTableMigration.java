package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateServerStorageTableMigration extends Migration {

    @Override
    public void up() {
        create("%prefix%storages", table -> {
            table.string("name", 255).primary();
            table.longText("content");
            table.timestamps();
        });
    }
}
