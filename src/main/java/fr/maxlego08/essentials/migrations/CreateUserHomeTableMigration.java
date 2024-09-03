package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class CreateUserHomeTableMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%user_homes", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("name", 255).primary();
            table.longText("location");
            table.string("material", 255).nullable();
            table.timestamps();
        });
    }
}
