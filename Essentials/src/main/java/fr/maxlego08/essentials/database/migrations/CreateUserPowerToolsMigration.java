package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class CreateUserPowerToolsMigration extends Migration {

    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%user_power_tools", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.string("material", 255);
            table.longText("command");
            table.timestamps();
        });
    }
}
