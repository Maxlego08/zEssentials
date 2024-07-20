package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class CreateUserMailBoxMigration extends Migration {

    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%user_mail_boxes", table -> {
            table.autoIncrement("id");
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.longText("itemstack");
            table.timestamp("expired_at");
            table.timestamps();
        });
    }
}
