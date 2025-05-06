package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateSanctionsTableMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%sanctions", table -> {
            table.autoIncrement("id");
            table.uuid("player_unique_id").foreignKey("%prefix%users", "unique_id", true);
            table.uuid("sender_unique_id");
            table.string("sanction_type", 255);
            table.longText("reason");
            table.bigInt("duration");
            table.timestamp("expired_at");
            table.timestamps();
        });
    }
}
