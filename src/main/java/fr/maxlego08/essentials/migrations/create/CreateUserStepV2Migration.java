package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserStepV2Migration extends Migration {
    @Override
    public void up() {
        create("%prefix%steps", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("step_name", 255).primary();
            table.json("data");
            table.bigInt("play_time").defaultValue(0);
            table.timestamp("finished_at").nullable();
            table.timestamps();
        });
    }
}
