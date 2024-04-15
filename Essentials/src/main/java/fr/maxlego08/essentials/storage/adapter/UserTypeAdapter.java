package fr.maxlego08.essentials.storage.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.HomeDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.LocationUtils;
import org.bukkit.Location;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserTypeAdapter extends TypeAdapter<User> {

    private final EssentialsPlugin plugin;
    private final LocationUtils locationUtils = new LocationUtils() {
    };

    public UserTypeAdapter(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("uniqueId").value(value.getUniqueId().toString());
        out.name("name").value(value.getName());
        if (value.getLastLocation() != null) {
            out.name("lastLocation").value(locationUtils.locationAsString(value.getLastLocation()));
        }

        out.name("options").beginObject();
        for (Map.Entry<Option, Boolean> entry : value.getOptions().entrySet()) {
            if (entry.getValue()) {
                out.name(entry.getKey().name()).value(entry.getValue());
            }
        }
        out.endObject();

        out.name("cooldowns").beginObject();
        for (Map.Entry<String, Long> entry : value.getCooldowns().entrySet()) {
            out.name(entry.getKey()).value(entry.getValue());
        }
        out.endObject();

        out.name("balances").beginObject();
        for (Map.Entry<String, BigDecimal> entry : value.getBalances().entrySet()) {
            out.name(entry.getKey()).value(entry.getValue());
        }
        out.endObject();

        out.name("homes").beginArray();
        for (Home home : value.getHomes()) {
            out.beginObject();
            out.name("name").value(home.getName());
            out.name("location").value(locationUtils.locationAsString(home.getLocation()));
            if (home.getMaterial() != null) {
                out.name("material").value(home.getMaterial().name());
            }
            out.endObject();
        }
        out.endArray();


        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        String name = null;
        UUID uniqueId = null; // Temporary storage for the UUID
        Map<Option, Boolean> options = new HashMap<>();
        Map<String, Long> cooldowns = new HashMap<>();
        Map<String, BigDecimal> balances = new HashMap<>();
        List<HomeDTO> homeDTOS = new ArrayList<>();
        Location lastLocation = null;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "uniqueId" -> uniqueId = UUID.fromString(in.nextString());
                case "name" -> name = in.nextString();
                case "lastLocation" -> lastLocation = locationUtils.stringAsLocation(in.nextString());
                case "options" -> {
                    in.beginObject();
                    while (in.hasNext()) {
                        options.put(Option.valueOf(in.nextName()), in.nextBoolean());
                    }
                    in.endObject();
                }
                case "cooldowns" -> {
                    in.beginObject();
                    while (in.hasNext()) {
                        cooldowns.put(in.nextName(), in.nextLong());
                    }
                    in.endObject();
                }
                case "balances" -> {
                    in.beginObject();
                    while (in.hasNext()) {
                        String key = in.nextName();
                        BigDecimal value = new BigDecimal(in.nextString());
                        balances.put(key, value);
                    }
                    in.endObject();
                }
                case "homes" -> {
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginObject();
                        String homeName = null;
                        String location = null;
                        String material = null;
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "name" -> homeName = in.nextString();
                                case "location" -> location = in.nextString();
                                case "material" -> material = in.nextString();
                            }
                        }
                        in.endObject();
                        homeDTOS.add(new HomeDTO(location, homeName, material));
                    }
                    in.endArray();
                }

            }
        }
        in.endObject();

        // Ensure that uniqueId is not null before creating a ZUser
        if (uniqueId == null) {
            throw new IOException("UniqueId is missing from the JSON input.");
        }
        User user = new ZUser(this.plugin, uniqueId); // Create the ZUser here

        // Now, set the other properties
        user.setName(name);
        user.setOptions(options.entrySet().stream().map(entry -> new OptionDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
        user.setCooldowns(cooldowns.entrySet().stream().map(entry -> new CooldownDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
        user.setLastLocation(lastLocation);
        user.setHomes(homeDTOS);
        balances.forEach(user::setBalance);

        return user;
    }

}
