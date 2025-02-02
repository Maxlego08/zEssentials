package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class ReCreatePowerToolsMigration extends Migration {

    @Override
    public void up() {
        create("%prefix%user_power_tools", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users").primary();
            table.string("material", 255).primary();
            table.longText("command");
            table.timestamps();
        });
    }
}
