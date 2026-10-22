package fr.maxlego08.essentials.migrations.update;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddSanctionColumns extends Migration {

    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> {
            // ToDo, need to fix for MARIADB ! Its work on MYSQL
            schema.bigInt("ban_sanction_id").nullable();//.foreignKey("%prefix%sanctions", "id", false);
            schema.bigInt("mute_sanction_id").nullable();//.foreignKey("%prefix%sanctions", "id", false);
        });
    }
}
