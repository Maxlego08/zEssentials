package fr.maxlego08.essentials.zutils.utils.component.components;

// ToDo REWORK Hover, doesnt work very well

import fr.maxlego08.essentials.zutils.utils.component.AbstractComponent;
import fr.maxlego08.essentials.zutils.utils.component.adapters.HoverAdapter;

public class HoverEvent {

    private final String action;
    private final Object value;

    public HoverEvent(String action, Object value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Constructs a new HoverEvent from an Adventure HoverEvent.
     *
     * @param event the Adventure HoverEvent. Cannot be null.
     * @throws NullPointerException if the event is null.
     */
    public HoverEvent(net.kyori.adventure.text.event.HoverEvent event) {
        this(event.action().toString(), getValueFromEvent(event));
    }

    /**
     * Extracts the value from an Adventure HoverEvent based on its action.
     *
     * @param event the Adventure HoverEvent. Cannot be null.
     * @return the extracted value.
     */
    private static Object getValueFromEvent(net.kyori.adventure.text.event.HoverEvent event) {
        return switch (event.action().toString()) {
            case "show_achievement" -> new TextComponent(event.value().toString());
            case "show_item" ->
                    new HoverAdapter.ShowItem((net.kyori.adventure.text.event.HoverEvent.ShowItem) event.value());
            case "show_entity" ->
                    new HoverAdapter.ShowEntity((net.kyori.adventure.text.event.HoverEvent.ShowEntity) event.value());
            default -> new TextComponent((net.kyori.adventure.text.TextComponent) event.value());
        };
    }

    public String getAction() {
        return action;
    }

    public Object getValue() {
        return value;
    }

    /**
     * Converts the value to a string representation suitable for MiniMessage.
     *
     * @param value the value to convert.
     * @return the string representation of the value.
     * @throws IllegalArgumentException if the value type is unsupported.
     */
    private String getValueAsString(Object value) {
        if (value instanceof String) return (String) value;
        if (value instanceof AbstractComponent component) return component.toMiniMessage();
        if (value instanceof HoverAdapter.ShowItem showItem) return showItem.toMiniMessage();
        if (value instanceof HoverAdapter.ShowEntity showEntity) return showEntity.toMiniMessage();
        throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
    }

    /**
     * Converts this HoverEvent to a MiniMessage string representation.
     *
     * @return the MiniMessage string representation of this HoverEvent.
     */
    public String toMiniMessage() {
        return String.format("<hover:%s:\"%s\">", this.action, getValueAsString(this.value));
    }

}
