package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserOptionTableMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%user_options", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.string("option_name", 255);
            table.bool("option_value");
            table.timestamps();
        });
    }
}
