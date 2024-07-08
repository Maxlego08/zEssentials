package fr.maxlego08.essentials.protocollib.component.components;

import fr.maxlego08.essentials.protocollib.component.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public final class DefaultComponent extends AbstractComponent {

    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(StandardTags.defaults()).build();
    private final Component component;

    public DefaultComponent(final Component component) {
        this.component = component;
    }

    public DefaultComponent(final String json) {
        this.component = GSON_SERIALIZER.deserialize(json);
    }

    @Override
    public String toMiniMessage() {
        return MINI_MESSAGE.serialize(component);
    }
}