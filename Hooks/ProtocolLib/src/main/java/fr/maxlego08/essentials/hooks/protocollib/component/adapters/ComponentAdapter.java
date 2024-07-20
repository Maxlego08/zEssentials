package fr.maxlego08.essentials.hooks.protocollib.component.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.hooks.protocollib.component.AbstractComponent;

import java.io.IOException;
import java.util.List;

/**
 * Abstract class to adapt components for JSON serialization and deserialization.
 *
 * @param <T> the type of component to adapt, which must extend {@link AbstractComponent}.
 */
public abstract class ComponentAdapter<T extends AbstractComponent> extends TypeAdapter<T> {

    /**
     * Writes the extra components of a component to a JSON writer.
     *
     * @param out       the JSON writer.
     * @param component the component whose extras are to be written.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeExtras(JsonWriter out, T component) throws IOException {
        this.writeComponents(out, "extra", component.getExtra());
    }

    /**
     * Writes a list of components to a JSON writer with a given name.
     *
     * @param out                the JSON writer.
     * @param name               the name to use for the JSON array.
     * @param abstractComponents the list of components to write.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeComponents(JsonWriter out, String name, List<AbstractComponent> abstractComponents) throws IOException {
        if (!abstractComponents.isEmpty()) {
            out.name(name);
            out.beginArray();
            for (AbstractComponent extraComponent : abstractComponents) {
                AbstractComponent.GSON.toJson(extraComponent, AbstractComponent.class, out);
            }
            out.endArray();
        }
    }

    /**
     * Reads the extra components of a component from a JSON object.
     *
     * @param jsonObject the JSON object containing the component data.
     * @param component  the component to which the extras are to be added.
     */
    protected void readExtras(JsonObject jsonObject, T component) {
        readComponents(jsonObject, "extra", component.getExtra());
    }

    /**
     * Reads a list of components from a JSON object with a given name and adds them to a list.
     *
     * @param jsonObject         the JSON object containing the component data.
     * @param name               the name of the JSON array.
     * @param abstractComponents the list to which the components are to be added.
     */
    protected void readComponents(JsonObject jsonObject, String name, List<AbstractComponent> abstractComponents) {
        if (jsonObject.has(name)) {
            JsonArray extraArray = jsonObject.getAsJsonArray(name);
            for (JsonElement element : extraArray) {
                AbstractComponent extraComponent = AbstractComponent.parse(element);
                if (extraComponent != null) {
                    abstractComponents.add(extraComponent);
                }
            }
        }
    }
}
