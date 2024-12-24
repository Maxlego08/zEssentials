package fr.maxlego08.essentials.bot.storage.migrations;

import fr.maxlego08.essentials.bot.utils.Tables;
import fr.maxlego08.sarah.database.Migration;

public class LinkAccountMigration extends Migration {
    @Override
    public void up() {

        this.create(Tables.LINK_ACCOUNTS, table -> {
            table.bigInt("user_id").primary();
            table.uuid("unique_id").primary();
            table.string("username", 16);
        });
    }
}
