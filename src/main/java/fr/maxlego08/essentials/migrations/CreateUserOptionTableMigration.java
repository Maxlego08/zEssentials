package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class CreateUserOptionTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%user_options", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("option_name", 255);
            table.bool("option_value");
            table.timestamps();
        });
    }
}
