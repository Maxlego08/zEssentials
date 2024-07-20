package fr.maxlego08.essentials.hooks.protocollib.component.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.hooks.protocollib.component.AbstractComponent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.HoverEvent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.TextComponent;

import java.io.IOException;
import java.util.UUID;

// ToDo REWORK Hover, doesnt work very well
public class HoverAdapter extends TypeAdapter<HoverEvent> {

    @Override
    public void write(JsonWriter out, HoverEvent event) throws IOException {
        out.beginObject();
        out.name("action").value(event.getAction());

        final Object value = event.getValue();
        if (value != null) {
            out.name("value");
            if (value instanceof String) {
                out.value((String) value);
            } else if (value instanceof TextComponent textComponent) {
                AbstractComponent.GSON.toJson(textComponent.toJsonElement(), out);
            } else if (value instanceof ShowItem || value instanceof ShowEntity) {
                AbstractComponent.GSON.toJson(value, value.getClass(), out);
            }
        }
        out.endObject();
    }

    @Override
    public HoverEvent read(JsonReader in) throws IOException {
        String action = null;
        Object value = null;

        JsonObject jsonObject = AbstractComponent.GSON.fromJson(in, JsonObject.class);

        if (jsonObject.has("action")) {
            action = jsonObject.get("action").getAsString();
        }

        // TODO, need rework for show item and show entity
        // {"contents":{"id":"minecraft:diamond","tag":"{display:{Name:'{\"color\":\"yellow\",\"text\":\"Salut !\"}'}}"},"action":"show_item"}

        if (jsonObject.has("value") && action != null) {
            JsonElement element = jsonObject.get("value");
            switch (action) {
                default -> {
                    if (element.isJsonPrimitive()) {
                        value = new TextComponent(element.getAsString());
                    } else if (element.isJsonArray()) {
                        value = AbstractComponent.parse(element.getAsJsonArray());
                    } else if (element.isJsonObject()) {
                        value = AbstractComponent.GSON.fromJson(element, TextComponent.class);
                    }
                }
                case "show_achievement" -> value = element.getAsString();
                case "show_item" -> value = AbstractComponent.GSON.fromJson(element, ShowItem.class);
                case "show_entity" -> value = AbstractComponent.GSON.fromJson(element, ShowEntity.class);
            }
        } else if (jsonObject.has("contents")) {
            JsonElement element = jsonObject.get("contents");
            value = AbstractComponent.GSON.fromJson(element, TextComponent.class);
        }
        return new HoverEvent(action, value);
    }

    public static final class ShowItem {
        private String item;

        private int count;

        public ShowItem() {
        }

        public ShowItem(net.kyori.adventure.text.event.HoverEvent.ShowItem value) {
            this.item = value.item().value();
            this.count = value.count();
        }

        public String toMiniMessage() {
            return this.item + ":" + this.count;
        }
    }

    public static final class ShowEntity {

        private String type;
        private UUID id;
        private AbstractComponent name;

        public ShowEntity() {
        }

        public ShowEntity(net.kyori.adventure.text.event.HoverEvent.ShowEntity value) {
            this.type = value.type().value();
            this.id = value.id();
            if (value.name() != null) this.name = AbstractComponent.parse(value.name());
        }

        public String toMiniMessage() {
            return this.type + ":" + this.id + ":\"" + this.name.toMiniMessage() + "\"";
        }
    }
}