package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateUserPlayTimeTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%user_play_times", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.bigInt("play_time");
            table.timestamps();
        });
    }
}
