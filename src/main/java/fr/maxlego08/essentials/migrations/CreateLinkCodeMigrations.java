package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateLinkCodeMigrations extends Migration {
    @Override
    public void up() {
        this.create("%prefix%link_codes", table -> {
            table.string("code", 255).primary();
            table.bigInt("user_id").primary();
            table.string("discord_name", 32);
            table.timestamps();
        });
    }
}
