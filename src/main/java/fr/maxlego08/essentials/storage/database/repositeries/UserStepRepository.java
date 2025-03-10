package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.StepDTO;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.UUID;

public class UserStepRepository extends Repository {

    public UserStepRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "steps");
    }

    public void insert(UUID uniqueId, Step step, String data) {
        this.insert(table -> {
            table.uuid("unique_id", uniqueId).primary();
            table.string("step_name", step.name()).primary();
            table.string("data", data);
        });
    }

    public StepDTO selectStep(UUID uniqueId, Step step) {
        var elements = this.select(StepDTO.class, table -> {
            table.where("unique_id", uniqueId);
            table.where("step_name", step.name());
        });
        return elements.isEmpty() ? null : elements.getFirst();
    }
}
