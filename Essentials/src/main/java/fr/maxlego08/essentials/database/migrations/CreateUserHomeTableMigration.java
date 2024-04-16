package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateUserHomeTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%player_homes", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%players");
            table.string("name", 255).primary();
            table.longText("location");
            table.string("material", 255).nullable();
            table.timestamps();
        });
    }
}
