package fr.maxlego08.essentials.api.steps;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;

public interface CustomStep {

    /**
     * Returns the name of the service that provides this custom step.
     * The name must be unique among all the services.
     * The name is case-sensitive.
     * The name is never null.
     *
     * @return the name of the service
     */
    String getServiceName();

    /**
     * Registers a custom step for the given player.
     * This method is called when the player has reached the step.
     * The {@code previousDate} parameter is the date of the latest step
     * that the player has reached.
     * The method must return a map containing all the information
     * that the step needs to save.
     * The method can return an empty map if the step does not
     * need to save any information.
     *
     * @param player      the player that has reached the step
     * @param previousDate the date of the previous step
     * @return a map containing all the information that the step needs to save
     */
    Map<String, Object> register(Player player, Date previousDate);

}
