package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserEconomyMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%economies", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("economy_name", 255).primary();
            table.decimal("amount", 65, 2).defaultValue("0");
            table.timestamps();
        });
    }
}
