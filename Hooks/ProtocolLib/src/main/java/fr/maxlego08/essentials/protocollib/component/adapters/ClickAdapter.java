package fr.maxlego08.essentials.protocollib.component.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.protocollib.component.components.ClickEvent;

import java.io.IOException;

/**
 * A Gson type adapter for serializing and deserializing {@link ClickEvent} objects.
 */
public class ClickAdapter extends TypeAdapter<ClickEvent> {

    /**
     * Serializes the specified {@link ClickEvent} into its JSON representation.
     *
     * @param out        the JSON writer.
     * @param clickEvent the {@link ClickEvent} to serialize.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(JsonWriter out, ClickEvent clickEvent) throws IOException {
        out.beginObject();
        out.name("action").value(clickEvent.getAction());
        out.name("value").value(clickEvent.getValue());
        out.endObject();
    }

    /**
     * Deserializes the JSON representation of a {@link ClickEvent}.
     *
     * @param in the JSON reader.
     * @return the deserialized {@link ClickEvent}.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public ClickEvent read(JsonReader in) throws IOException {
        String action = net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND.name();
        String value = "/say Hummm, its doesnt work !";

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "action" -> action = in.nextString();
                case "value" -> value = in.nextString();
                default -> in.skipValue();
            }
        }
        in.endObject();
        return new ClickEvent(action, value);
    }
}