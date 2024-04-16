package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateUserOptionTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%player_options", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%players");
            table.string("option_name", 255);
            table.bool("option_value");
            table.timestamps();
        });
    }
}
