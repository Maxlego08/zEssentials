package fr.maxlego08.essentials.migrations.drop;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class DropStepMigration extends Migration {

    @Override
    public void up() {
        SchemaBuilder.drop(this, "%prefix%steps");
    }
}
