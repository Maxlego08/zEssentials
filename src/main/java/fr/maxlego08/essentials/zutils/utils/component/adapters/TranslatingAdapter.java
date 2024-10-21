package fr.maxlego08.essentials.zutils.utils.component.adapters;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.zutils.utils.component.AbstractComponent;
import fr.maxlego08.essentials.zutils.utils.component.components.TranslatingComponent;

import java.io.IOException;

/**
 * A Gson type adapter for serializing and deserializing {@link TranslatingComponent} objects.
 */
public class TranslatingAdapter extends ComponentAdapter<TranslatingComponent> {

    /**
     * Serializes the specified {@link TranslatingComponent} into its JSON representation.
     *
     * @param out       the JSON writer.
     * @param component the {@link TranslatingComponent} to serialize.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(JsonWriter out, TranslatingComponent component) throws IOException {
        out.beginObject();
        out.name("translate").value(component.getKey());

        if (component.getColor() != null) {
            out.name("color").value(component.getColor());
        }

        this.writeComponents(out, "with", component.getWith());
        this.writeExtras(out, component);

        out.endObject();
    }

    /**
     * Deserializes the JSON representation of a {@link TranslatingComponent}.
     *
     * @param in the JSON reader.
     * @return the deserialized {@link TranslatingComponent}.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public TranslatingComponent read(JsonReader in) throws IOException {
        JsonObject jsonObject = AbstractComponent.GSON.fromJson(in, JsonObject.class);
        String key = jsonObject.has("translate") ? jsonObject.get("translate").getAsString() : null;
        String color = jsonObject.has("color") ? jsonObject.get("color").getAsString() : null;
        TranslatingComponent component = new TranslatingComponent(key, color);

        this.readComponents(jsonObject, "with", component.getWith());
        this.readExtras(jsonObject, component);

        return component;
    }
}