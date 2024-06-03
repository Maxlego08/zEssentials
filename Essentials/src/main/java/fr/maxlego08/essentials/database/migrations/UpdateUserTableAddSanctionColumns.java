package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddSanctionColumns extends Migration {

    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> {
            schema.bigInt("ban_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
            schema.bigInt("mute_sanction_id").nullable().foreignKey("%prefix%sanctions", "id", false);
        });
    }
}
