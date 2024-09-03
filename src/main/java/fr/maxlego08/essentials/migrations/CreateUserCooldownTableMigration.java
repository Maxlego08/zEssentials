package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class CreateUserCooldownTableMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%user_cooldowns", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("cooldown_name", 255).primary();
            table.bigInt("cooldown_value");
            table.timestamps();
        });
    }
}
