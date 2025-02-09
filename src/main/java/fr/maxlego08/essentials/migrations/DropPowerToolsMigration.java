package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class DropPowerToolsMigration extends Migration {

    @Override
    public void up() {
        SchemaBuilder.drop(this, "%prefix%user_power_tools");
    }
}
