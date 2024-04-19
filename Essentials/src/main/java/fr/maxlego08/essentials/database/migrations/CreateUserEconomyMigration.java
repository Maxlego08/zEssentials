package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateUserEconomyMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%economies", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("economy_name", 255).primary();
            table.decimal("amount", 65, 2).defaultValue("0");
            table.timestamps();
        });
    }
}
