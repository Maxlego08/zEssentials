package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreatePlayerVault extends Migration {
    @Override
    public void up() {
        create("%prefix%vaults", table -> {
            table.uuid("unique_id").primary();
            table.bigInt("vault_id").primary();
            table.string("name", 255);
            table.longText("icon").nullable();
        });
    }
}
