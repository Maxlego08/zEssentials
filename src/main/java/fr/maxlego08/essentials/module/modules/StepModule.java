package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.event.events.step.StepRegisterEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.steps.PlayerStep;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.api.steps.StepManager;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class StepModule extends ZModule implements StepManager {

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
    public void handleStep(CommandSender sender, Player player, Step step) {

        IStorage iStorage = plugin.getStorageManager().getStorage();
        iStorage.canCreateStep(player.getUniqueId(), step, result -> {
            if (!result) {
                message(sender, Message.STEP_ALREADY_EXIST, "%step%", step.name());
                return;
            }

            this.plugin.getScheduler().runNextTick(wrappedTask -> createStep(this.plugin.getUser(player.getUniqueId()), player, step));
        });
    }

    private void createStep(User user, Player player, Step step) {

        PlayerStep playerStep = new PlayerStep(player);

        StepRegisterEvent event = new StepRegisterEvent(user, playerStep);
        event.callEvent();

        if (event.isCancelled()) return;

        IStorage iStorage = plugin.getStorageManager().getStorage();
        var jsonResult = this.plugin.getGson().toJson(playerStep);
        iStorage.registerStep(player.getUniqueId(), step, jsonResult);
    }
}
