package fr.maxlego08.essentials.hooks.protocollib.component.adapters;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.hooks.protocollib.component.AbstractComponent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.KeyBindComponent;

import java.io.IOException;

/**
 * A Gson type adapter for serializing and deserializing {@link KeyBindComponent} objects.
 */
public class KeyBindAdapter extends ComponentAdapter<KeyBindComponent> {

    /**
     * Serializes the specified {@link KeyBindComponent} into its JSON representation.
     *
     * @param out       the JSON writer.
     * @param component the {@link KeyBindComponent} to serialize.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(JsonWriter out, KeyBindComponent component) throws IOException {
        out.beginObject();

        out.name("keybind").value(component.getKeybind());
        this.writeExtras(out, component);

        out.endObject();
    }

    /**
     * Deserializes the JSON representation of a {@link KeyBindComponent}.
     *
     * @param in the JSON reader.
     * @return the deserialized {@link KeyBindComponent}.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public KeyBindComponent read(JsonReader in) throws IOException {
        JsonObject jsonObject = AbstractComponent.GSON.fromJson(in, JsonObject.class);

        String keybind = null;
        if (jsonObject.has("keybind")) {
            keybind = jsonObject.get("keybind").getAsString();
        }

        KeyBindComponent component = new KeyBindComponent(keybind);
        this.readExtras(jsonObject, component);

        return component;
    }
}