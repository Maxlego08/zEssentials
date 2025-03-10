package fr.maxlego08.essentials.api.steps;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface StepManager {

    void handleStep(CommandSender sender, Player player, Step step);

    Optional<Step> getStep(String stepName);

    List<Step> getSteps();

    void registerStep(CustomStep customStep);
}
