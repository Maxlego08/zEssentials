package fr.maxlego08.essentials.api.storage.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.utils.SafeLocation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LocationAdapter extends TypeAdapter<SafeLocation> {

    private static final Type seriType = new TypeToken<Map<String, Object>>() {
    }.getType();
    private static final String NAME = "name";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String YAW = "yaw";
    private static final String PITCH = "pitch";
    private final EssentialsPlugin plugin;

    public LocationAdapter(EssentialsPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public void write(JsonWriter jsonWriter, SafeLocation location) throws IOException {
        if (location == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(getRaw(location));
    }

    @Override
    public SafeLocation read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return fromRaw(jsonReader.nextString());
    }

    private String getRaw(SafeLocation location) {
        Map<String, Object> serial = new HashMap<>();
        serial.put(NAME, location.getWorld());
        serial.put(X, Double.toString(location.getX()));
        serial.put(Y, Double.toString(location.getY()));
        serial.put(Z, Double.toString(location.getZ()));
        serial.put(YAW, Float.toString(location.getYaw()));
        serial.put(PITCH, Float.toString(location.getPitch()));
        return plugin.getGson().toJson(serial);
    }

    private SafeLocation fromRaw(String raw) {
        Map<String, Object> keys = this.plugin.getGson().fromJson(raw, seriType);
        String worldName = (String) keys.get(NAME);
        return new SafeLocation(worldName, Double.parseDouble((String) keys.get(X)), Double.parseDouble((String) keys.get(Y)), Double.parseDouble((String) keys.get(Z)), Float.parseFloat((String) keys.get(YAW)), Float.parseFloat((String) keys.get(PITCH)));
    }

}
