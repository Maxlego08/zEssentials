package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserStepMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%steps", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("step_name", 255).primary();
            table.json("data");
            table.timestamps();
        });
    }
}
