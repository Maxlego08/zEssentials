package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddVoteColumn extends Migration {
    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> {
            schema.bigInt("vote").defaultValue(0);
            schema.integer("vote_offline").defaultValue(0);
        });
    }
}
