package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateCommandsMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%commands", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.longText("command");
            table.timestamps();
        });
    }
}
