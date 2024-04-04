package fr.maxlego08.essentials.commands.commands.weather;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandPlayerWeather extends VCommand {

    public CommandPlayerWeather(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_PLAYER_WEATHER);
        this.setDescription(Message.DESCRIPTION_PLAYER_WEATHER);
        this.addOptionalArg("weather", (a, b) -> Arrays.asList("sun", "storm", "thunder", "clear"));
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String weather = argAsString(0, null);

        if (weather == null || weather.equalsIgnoreCase("sun") || weather.equalsIgnoreCase("clear")) {

            this.player.resetPlayerWeather();
            message(sender, Message.COMMAND_PLAYER_WEATHER_RESET);
        } else if (weather.equalsIgnoreCase("storm") || weather.equalsIgnoreCase("thunder")) {

            this.player.setPlayerWeather(WeatherType.DOWNFALL);
            message(sender, Message.COMMAND_PLAYER_WEATHER_DOWNFALL);
        }

        return CommandResultType.SUCCESS;
    }
}
