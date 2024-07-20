package fr.maxlego08.essentials.hooks.protocollib.component.adapters;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.maxlego08.essentials.hooks.protocollib.component.AbstractComponent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.ClickEvent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.HoverEvent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.TextComponent;

import java.io.IOException;

public class TextAdapter extends ComponentAdapter<TextComponent> {

    @Override
    public void write(JsonWriter out, TextComponent component) throws IOException {
        out.beginObject();

        if (component.getText() != null) out.name("text").value(component.getText());
        if (component.getColor() != null) out.name("color").value(component.getColor());
        if (component.isBold()) out.name("bold").value(true);
        if (component.isItalic() || component.isForceUnItalic()) out.name("italic").value(component.isItalic());
        if (component.isUnderlined()) out.name("underlined").value(true);
        if (component.isStrikethrough()) out.name("strikethrough").value(true);
        if (component.isObfuscated()) out.name("obfuscated").value(true);
        if (component.getInsertion() != null) out.name("insertion").value(component.getInsertion());

        if (component.getClickEvent() != null) {
            out.name("clickEvent");
            AbstractComponent.GSON.toJson(component.getClickEvent(), ClickEvent.class, out);
        }

        if (component.getHoverEvent() != null) {
            out.name("hoverEvent");
            AbstractComponent.GSON.toJson(component.getHoverEvent(), HoverEvent.class, out);
        }

        this.writeExtras(out, component);

        out.endObject();
    }

    @Override
    public TextComponent read(JsonReader in) throws IOException {
        JsonObject jsonObject = AbstractComponent.GSON.fromJson(in, JsonObject.class);
        String text = jsonObject.has("text") ? jsonObject.get("text").getAsString() : null;
        String color = jsonObject.has("color") ? jsonObject.get("color").getAsString() : null;
        boolean bold = jsonObject.has("bold") && jsonObject.get("bold").getAsBoolean();
        boolean italic = jsonObject.has("italic") && jsonObject.get("italic").getAsBoolean();
        boolean underlined = jsonObject.has("underlined") && jsonObject.get("underlined").getAsBoolean();
        boolean strikethrough = jsonObject.has("strikethrough") && jsonObject.get("strikethrough").getAsBoolean();
        boolean obfuscated = jsonObject.has("obfuscated") && jsonObject.get("obfuscated").getAsBoolean();
        String insertion = jsonObject.has("insertion") ? jsonObject.get("insertion").getAsString() : null;
        ClickEvent clickEvent = jsonObject.has("clickEvent") ? AbstractComponent.GSON.fromJson(jsonObject.get("clickEvent"), ClickEvent.class) : null;
        HoverEvent hoverEvent = jsonObject.has("hoverEvent") ? AbstractComponent.GSON.fromJson(jsonObject.get("hoverEvent"), HoverEvent.class) : null;

        TextComponent component = new TextComponent(text, color, bold, italic, underlined, strikethrough, obfuscated, false, insertion, clickEvent, hoverEvent);
        readExtras(jsonObject, component);

        return component;
    }

}