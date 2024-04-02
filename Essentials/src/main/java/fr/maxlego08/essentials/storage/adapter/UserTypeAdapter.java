package fr.maxlego08.essentials.storage.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.ZUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserTypeAdapter extends TypeAdapter<User> {

    private final EssentialsPlugin plugin;

    public UserTypeAdapter(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("uniqueId").value(value.getUniqueId().toString());
        out.name("name").value(value.getName());

        out.name("options").beginObject();
        for (Map.Entry<Option, Boolean> entry : value.getOptions().entrySet()) {
            out.name(entry.getKey().name()).value(entry.getValue());
        }
        out.endObject();

        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        UUID uniqueId = null;
        String name = null;
        Map<Option, Boolean> options = new HashMap<>();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "uniqueId" -> uniqueId = UUID.fromString(in.nextString());
                case "name" -> name = in.nextString();
                case "options" -> {
                    in.beginObject();
                    while (in.hasNext()) {
                        options.put(Option.valueOf(in.nextName()), in.nextBoolean());
                    }
                    in.endObject();
                }
            }
        }
        in.endObject();

        User user = new ZUser(this.plugin, uniqueId);
        user.setName(name);
        options.forEach(user::setOption);
        return user;
    }
}
