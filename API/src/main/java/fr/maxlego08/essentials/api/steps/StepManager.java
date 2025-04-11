package fr.maxlego08.essentials.api.steps;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface StepManager {

    /**
     * Handles the specified step for a given player.
     *
     * @param sender The command sender who initiated the step handling.
     * @param player The player for whom the step is being handled.
     * @param step   The step to be handled.
     */
    void handleStep(CommandSender sender, Player player, Step step);

    /**
     * Gets a step by its name.
     *
     * @param stepName The name of the step to get.
     * @return An optional containing the step if it exists, or an empty optional if it does not.
     */
    Optional<Step> getStep(String stepName);

    /**
     * Gets all the steps registered in the step manager.
     *
     * @return a list of steps.
     */
    List<Step> getSteps();

    /**
     * Registers a custom step for the step manager.
     *
     * @param customStep The custom step to register.
     */
    void registerStep(CustomStep customStep);
}
