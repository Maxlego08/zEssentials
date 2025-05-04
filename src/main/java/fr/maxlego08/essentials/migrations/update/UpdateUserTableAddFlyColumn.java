package fr.maxlego08.essentials.migrations.update;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddFlyColumn extends Migration {
    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> schema.bigInt("fly_seconds").defaultValue(0));
    }
}
