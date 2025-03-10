package fr.maxlego08.essentials.api.steps;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;

public interface CustomStep {

    String getServiceName();

    Map<String, Object> register(Player player, Date previousDate);

}
