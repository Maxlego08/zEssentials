package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateEconomyTransactionMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create("%prefix%economy_transactions", table -> {
            table.uuid("from_unique_id");
            table.uuid("to_unique_id");
            table.string("economy_name", 255);
            table.decimal("amount", 65, 2).defaultValue("0");
            table.decimal("from_amount", 65, 2).defaultValue("0");
            table.decimal("to_amount", 65, 2).defaultValue("0");
            table.timestamps();
        });
    }
}
