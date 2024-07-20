package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

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
