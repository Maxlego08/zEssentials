package fr.maxlego08.essentials.protocollib.component.components;

import fr.maxlego08.essentials.protocollib.component.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;

/**
 * Represents a component for keybindings in chat messages.
 * This component can hold other extra components within it.
 */
public final class KeyBindComponent extends AbstractComponent {

    /**
     * The keybinding string.
     */
    private final String keybind;

    /**
     * Constructs a new {@link KeyBindComponent} with the specified keybinding.
     *
     * @param keybind the keybinding string.
     */
    public KeyBindComponent(String keybind) {
        this.keybind = keybind;
    }

    /**
     * Constructs a new {@link KeyBindComponent} from an existing {@link KeybindComponent}.
     *
     * @param component the existing {@link KeybindComponent}.
     */
    public KeyBindComponent(final KeybindComponent component) {
        this.keybind = component.keybind();
        if (!component.children().isEmpty()) {
            for (final Component child : component.children()) {
                extra.add(AbstractComponent.parse(child));
            }
        }
    }

    /**
     * Gets the keybinding string of this component.
     *
     * @return the keybinding string.
     */
    public String getKeybind() {
        return keybind;
    }

    /**
     * Converts this component and its extra components to a MiniMessage string.
     *
     * @return the MiniMessage representation of this component.
     */
    @Override
    public String toMiniMessage() {
        final StringBuilder builder = new StringBuilder();
        builder.append("<key:").append(keybind).append(">");
        for (final AbstractComponent extra : this.extra) {
            builder.append(extra.toMiniMessage());
        }
        return builder.toString();
    }
}