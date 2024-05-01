package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddSanctionColumns extends Migration {

    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> {
            schema.integer("ban_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
            schema.integer("mute_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
        });
    }
}
