package fr.maxlego08.essentials.hooks.protocollib.component.components;

import fr.maxlego08.essentials.hooks.protocollib.component.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a translating component that can hold other components and supports translation keys.
 */
public class TranslatingComponent extends AbstractComponent {

    /**
     * List of components used as arguments for the translation key.
     */
    private final List<AbstractComponent> with = new LinkedList<>();

    /**
     * The translation key.
     */
    private final String key;

    /**
     * The color of the component text.
     */
    private String color;

    /**
     * Constructs a new {@link TranslatingComponent} with the specified translation key and color.
     *
     * @param key   the translation key.
     * @param color the color of the component text.
     */
    public TranslatingComponent(String key, String color) {
        this.key = key;
        this.color = color;
    }

    /**
     * Constructs a new {@link TranslatingComponent} from an existing {@link TranslatableComponent}.
     *
     * @param component the existing {@link TranslatableComponent}.
     */
    public TranslatingComponent(final TranslatableComponent component) {
        this.key = component.key();

        final TextColor color = component.color();
        if (color != null) {
            this.color = color.asHexString();
        }

        final List<Component> args = ComponentLike.asComponents(component.arguments());
        if (!args.isEmpty()) {
            for (final Component arg : args) {
                with.add(AbstractComponent.parse(arg));
            }
        }

        if (!component.children().isEmpty()) {
            for (final Component child : component.children()) {
                extra.add(AbstractComponent.parse(child));
            }
        }
    }

    /**
     * Converts this component and its extra components to a MiniMessage string.
     *
     * @return the MiniMessage representation of this component.
     */
    @Override
    public String toMiniMessage() {
        StringBuilder builder = new StringBuilder();

        if (color != null) {
            builder.append("<color:").append(color).append(">");
        }

        builder.append("<lang:").append(key);
        String withComponents = this.with.stream().map(component -> ":\"" + component.toMiniMessage() + "\"").collect(Collectors.joining());
        builder.append(withComponents).append(">");
        builder.append(this.getExtraAsMiniMessage());

        if (color != null) {
            builder.append("</color>");
        }
        return builder.toString();
    }

    /**
     * Gets the translation key of this component.
     *
     * @return the translation key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the color of this component text.
     *
     * @return the color of the component text.
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the list of components used as arguments for the translation key.
     *
     * @return a list of components.
     */
    public List<AbstractComponent> getWith() {
        return with;
    }
}