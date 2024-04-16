package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class UpdateUserTableAddSanctionColumns extends Migration {

    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%players", schema -> {
            schema.integer("ban_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
            schema.integer("mute_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
        });
    }
}
