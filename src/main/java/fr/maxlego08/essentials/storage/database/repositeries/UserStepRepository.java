package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.StepDTO;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.Date;
import java.util.UUID;

public class UserStepRepository extends Repository {

    public UserStepRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "steps");
    }

    public void createStep(UUID uniqueId, Step step, long playTime) {
        this.insert(table -> {
            table.uuid("unique_id", uniqueId).primary();
            table.string("step_name", step.name()).primary();
            table.bigInt("play_time_start", playTime);
        });
    }

    public void finishStep(UUID uniqueId, Step step, String data, long playTimeBetween, long playTimeEnd) {
        this.update(table -> {
            table.string("data", data);
            table.bigInt("play_time_end", playTimeEnd);
            table.bigInt("play_time_between", playTimeBetween);
            table.object("finished_at", new Date());
            table.where("unique_id", uniqueId);
            table.where("step_name", step.name());
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
