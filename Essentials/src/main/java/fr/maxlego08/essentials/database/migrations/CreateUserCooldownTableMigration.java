package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateUserCooldownTableMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create("%prefix%player_cooldowns", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%players");
            table.string("cooldown_name", 255).primary();
            table.bigInt("cooldown_value");
            table.timestamps();
        });
    }
}
