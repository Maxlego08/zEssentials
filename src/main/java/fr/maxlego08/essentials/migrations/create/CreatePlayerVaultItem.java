package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreatePlayerVaultItem extends Migration {
    @Override
    public void up() {
        create("%prefix%vault_items", table -> {
            table.uuid("unique_id").primary();
            table.bigInt("vault_id").primary();
            table.bigInt("slot").primary();
            table.longText("item");
            table.bigInt("quantity");
        });
    }
}
