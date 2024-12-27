package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateLinkHistoryMigration extends Migration {
    @Override
    public void up() {
        this.create("%prefix%link_histories", table -> {
            table.autoIncrement("id");
            table.string("action", 255);
            table.bigInt("discord_id").nullable();
            table.uuid("minecraft_id").nullable();
            table.string("discord_name", 32).nullable();
            table.string("minecraft_name", 16).nullable();
            table.longText("data").nullable();
            table.timestamps();
        });
    }
}
