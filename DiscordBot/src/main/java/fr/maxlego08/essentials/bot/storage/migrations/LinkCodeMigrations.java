package fr.maxlego08.essentials.bot.storage.migrations;

import fr.maxlego08.essentials.bot.utils.Tables;
import fr.maxlego08.sarah.database.Migration;

public class LinkCodeMigrations extends Migration {
    @Override
    public void up() {
        this.create(Tables.LINK_CODES, table -> {
            table.string("code", 255).primary();
            table.bigInt("user_id").primary();
        });
    }
}
