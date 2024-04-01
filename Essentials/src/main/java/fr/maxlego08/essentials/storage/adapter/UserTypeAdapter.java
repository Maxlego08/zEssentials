package fr.maxlego08.essentials.storage.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.api.User;
import fr.maxlego08.essentials.storage.ZUser;

import java.io.IOException;
import java.util.UUID;

public class UserTypeAdapter extends TypeAdapter<User> {

    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("uniqueId").value(value.getUniqueId().toString());
        out.name("name").value(value.getName());
        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        UUID uniqueId = null;
        String name = null;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "uniqueId" -> uniqueId = UUID.fromString(in.nextString());
                case "name" -> name = in.nextString();
            }
        }
        in.endObject();

        User user = new ZUser(uniqueId);
        user.setName(name);
        return user;
    }
}
