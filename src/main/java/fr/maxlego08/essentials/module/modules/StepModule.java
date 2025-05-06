package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.event.events.step.StepCreateEvent;
import fr.maxlego08.essentials.api.event.events.step.StepFinishEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.steps.CustomStep;
import fr.maxlego08.essentials.api.steps.PlayerStep;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.api.steps.StepManager;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StepModule extends ZModule implements StepManager {

    private final List<CustomStep> customSteps = new ArrayList<>();
    private List<Step> steps;

    public StepModule(ZEssentialsPlugin plugin) {
        super(plugin, "steps");
    }

    @Override
    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public Optional<Step> getStep(String stepName) {
        return this.steps.stream().filter(step -> step.name().equalsIgnoreCase(stepName)).findFirst();
    }

    @Override
    public void startStep(CommandSender sender, Player player, Step step) {

        var scheduler = this.plugin.getScheduler();
        scheduler.runAsync(w1 -> {

            IStorage iStorage = plugin.getStorageManager().getStorage();
            var stepDTO = iStorage.selectStep(player.getUniqueId(), step);
            if (stepDTO != null) {
                message(sender, Message.STEP_ALREADY_EXIST, "%step%", step.name());
                return;
            }

            scheduler.runNextTick(w2 -> createStep(this.plugin.getUser(player.getUniqueId()), player, step));
        });
    }

    @Override
    public void finishStep(CommandSender sender, Player player, Step step) {
        var scheduler = this.plugin.getScheduler();
        scheduler.runAsync(w1 -> {

            IStorage iStorage = plugin.getStorageManager().getStorage();
            var stepDTO = iStorage.selectStep(player.getUniqueId(), step);
            if (stepDTO == null) {
                message(sender, Message.STEP_DOESNT_STARTED, "%step%", step.name());
                return;
            }

            int indexAt = this.steps.indexOf(step);
            Date date = new Date();
            if (indexAt > 0) {
                Step previousStep = this.steps.get(indexAt - 1);
                var previousStepDTO = iStorage.selectStep(player.getUniqueId(), previousStep);
                if (previousStepDTO != null) {
                    date = previousStepDTO.created_at();
                }
            }

            Map<String, Object> additionalData = new HashMap<>();
            for (CustomStep customStep : this.customSteps) {
                additionalData.put(customStep.getServiceName(), customStep.register(player, date));
            }

            scheduler.runNextTick(w2 -> finishStep(plugin.getUser(player.getUniqueId()), player, step, additionalData, stepDTO.play_time_start()));
        });
    }

    /**
     * Finalizes a step for a given player by creating a PlayerStep object,
     * calling the StepFinishEvent, and updating the storage with the step's data.
     * If the event is cancelled, the process is halted.
     *
     * @param user           The user associated with the step.
     * @param player         The player for whom the step is being finished.
     * @param step           The step to finalize.
     * @param additionalData A map of additional data relevant to the step.
     * @param playTimeStart  The start time of the play session for the step.
     */
    private void finishStep(User user, Player player, Step step, Map<String, Object> additionalData, long playTimeStart) {

        PlayerStep playerStep = new PlayerStep(player, additionalData);

        StepFinishEvent event = new StepFinishEvent(user, playerStep);
        event.callEvent();

        if (event.isCancelled()) return;

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        var jsonResult = this.plugin.getGson().toJson(playerStep);
        long playTimeBetween = user.getPlayTime() - playTimeStart;
        iStorage.finishStep(player.getUniqueId(), step, jsonResult, user.getPlayTime(), playTimeBetween);
    }

    /**
     * Starts a step for a given player by creating a StepCreateEvent and,
     * if the event is not cancelled, updating the storage with the step's data.
     *
     * @param user   The user associated with the step.
     * @param player The player for whom the step is being started.
     * @param step   The step to start.
     */
    private void createStep(User user, Player player, Step step) {

        StepCreateEvent event = new StepCreateEvent(user, step);
        event.callEvent();

        if (event.isCancelled()) return;

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        iStorage.createStep(player.getUniqueId(), step, user.getPlayTime());
    }

    @Override
    public void registerStep(CustomStep customStep) {
        this.customSteps.removeIf(currentStep -> currentStep.getServiceName().equalsIgnoreCase(customStep.getServiceName()));
        this.customSteps.add(customStep);
    }
}
