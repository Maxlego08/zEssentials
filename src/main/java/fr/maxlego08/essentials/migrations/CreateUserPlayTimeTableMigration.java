package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class CreateUserPlayTimeTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%user_play_times", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.bigInt("play_time");
            table.string("address", 255);
            table.timestamps();
        });
    }
}
