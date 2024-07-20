package fr.maxlego08.essentials.hooks.protocollib.component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.maxlego08.essentials.hooks.protocollib.component.adapters.ClickAdapter;
import fr.maxlego08.essentials.hooks.protocollib.component.adapters.HoverAdapter;
import fr.maxlego08.essentials.hooks.protocollib.component.adapters.KeyBindAdapter;
import fr.maxlego08.essentials.hooks.protocollib.component.adapters.TextAdapter;
import fr.maxlego08.essentials.hooks.protocollib.component.adapters.TranslatingAdapter;
import fr.maxlego08.essentials.hooks.protocollib.component.components.ClickEvent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.DefaultComponent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.HoverEvent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.KeyBindComponent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.TextComponent;
import fr.maxlego08.essentials.hooks.protocollib.component.components.TranslatingComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract base class representing a component that can be serialized to and deserialized from JSON.
 * Based on <a href="https://github.com/itsme-to/ItsMyConfig/tree/main/src/main/java/to/itsme/itsmyconfig/component">ItsMyConfig</a>
 */
public abstract class AbstractComponent {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ClickEvent.class, new ClickAdapter())
            .registerTypeAdapter(HoverEvent.class, new HoverAdapter())
            .registerTypeAdapter(TextComponent.class, new TextAdapter())
            .registerTypeAdapter(KeyBindComponent.class, new KeyBindAdapter())
            .registerTypeAdapter(TranslatingComponent.class, new TranslatingAdapter())
            .create();

    protected final List<AbstractComponent> extra = new LinkedList<>();

    public static AbstractComponent parse(@NotNull final String json) {
        try {
            return parse(JsonParser.parseString(json));
        } catch (final Throwable ignored) {
            return new DefaultComponent(json);
        }
    }

    public static AbstractComponent parse(@NotNull final Component component) {
        if (component instanceof net.kyori.adventure.text.TextComponent) {
            return new TextComponent((net.kyori.adventure.text.TextComponent) component);
        } else if (component instanceof KeybindComponent) {
            return new KeyBindComponent((KeybindComponent) component);
        } else if (component instanceof TranslatableComponent) {
            return new TranslatingComponent((TranslatableComponent) component);
        }
        return new DefaultComponent(component);
    }

    public static AbstractComponent parse(@NotNull final JsonElement element) {
        if (element.isJsonObject()) {
            final JsonObject jsonObject = element.getAsJsonObject();
            if (jsonObject.has("keybind")) {
                return GSON.fromJson(element, KeyBindComponent.class);
            } else if (jsonObject.has("translate")) {
                return GSON.fromJson(element, TranslatingComponent.class);
            }
            return GSON.fromJson(element, TextComponent.class);
        } else if (element.isJsonArray()) {
            final TextComponent component = new TextComponent();
            for (final JsonElement found : element.getAsJsonArray()) {
                component.extra.add(parse(found));
            }
            return component;
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return new TextComponent(element.getAsString());
        }

        return new DefaultComponent(element.getAsString());
    }

    public abstract String toMiniMessage();

    public JsonElement toJsonElement() {
        return GSON.toJsonTree(this);
    }

    public List<AbstractComponent> getExtra() {
        return extra;
    }

    protected String getExtraAsMiniMessage() {
        return this.extra.stream().map(AbstractComponent::toMiniMessage).collect(Collectors.joining());
    }
}
