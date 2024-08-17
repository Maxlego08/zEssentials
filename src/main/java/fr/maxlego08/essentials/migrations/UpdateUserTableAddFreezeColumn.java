package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddFreezeColumn extends Migration {

    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> {
            schema.bool("frozen").nullable();
        });
    }
}
