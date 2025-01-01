package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateLinkAccountMigration extends Migration {
    @Override
    public void up() {

        this.create("%prefix%link_accounts", table -> {
            table.bigInt("user_id").primary();
            table.uuid("unique_id").primary();
            table.string("minecraft_name", 16);
            table.string("discord_name", 32);
            table.timestamps();
        });
    }
}
