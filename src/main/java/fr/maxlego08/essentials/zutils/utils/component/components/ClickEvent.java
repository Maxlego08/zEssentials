package fr.maxlego08.essentials.zutils.utils.component.components;

/**
 * The ClickEvent class represents a click event that can be associated with a text component.
 * This class is used to handle click actions and their corresponding values.
 */
public class ClickEvent {

    private final String action;
    private final String value;

    /**
     * Constructs a new ClickEvent with the specified action and value.
     *
     * @param action The action to be performed when the text component is clicked.
     * @param value  The value associated with the click action.
     */
    public ClickEvent(String action, String value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Constructs a new ClickEvent from an Adventure ClickEvent.
     *
     * @param event The Adventure ClickEvent.
     */
    public ClickEvent(net.kyori.adventure.text.event.ClickEvent event) {
        this.action = event.action().toString();
        this.value = getValue(event);
    }

    private static String getValue(net.kyori.adventure.text.event.ClickEvent event) {
        try {
            try {
                // Adventure 4.x exposes the value directly; Paper 1.21.4 still uses this API.
                return (String) net.kyori.adventure.text.event.ClickEvent.class.getMethod("value").invoke(event);
            } catch (NoSuchMethodException ignored) {
                // Adventure 5 uses typed payloads. Resolve their public interfaces so that the
                // common code does not link to classes absent from older Paper versions.
                Object payload = net.kyori.adventure.text.event.ClickEvent.class.getMethod("payload").invoke(event);
                boolean changePage = event.action().toString().equals("change_page");
                Class<?> payloadType = Class.forName(net.kyori.adventure.text.event.ClickEvent.class.getName() + "$Payload$" + (changePage ? "Int" : "Text"));
                return String.valueOf(payloadType.getMethod(changePage ? "integer" : "value").invoke(payload));
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to read the click event value", exception);
        }
    }

    /**
     * Gets the action of the click event.
     *
     * @return The action of the click event.
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the value associated with the click event.
     *
     * @return The value associated with the click event.
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts the ClickEvent to a MiniMessage format.
     *
     * @return The ClickEvent as a MiniMessage formatted string.
     */
    public String toMiniMessage() {
        return String.format("<click:%s:\"%s\">", this.action, this.value);
    }

}
