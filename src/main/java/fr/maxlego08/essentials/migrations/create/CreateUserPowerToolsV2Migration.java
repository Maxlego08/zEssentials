package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserPowerToolsV2Migration extends Migration {

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
