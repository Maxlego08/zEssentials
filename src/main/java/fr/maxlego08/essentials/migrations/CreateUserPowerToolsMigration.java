package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserPowerToolsMigration extends Migration {

    @Override
    public void up() {
        create("%prefix%user_power_tools", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.string("material", 255);
            table.longText("command");
            table.timestamps();
        });
    }
}
