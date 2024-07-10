package fr.maxlego08.essentials.protocollib.component.components;

import fr.maxlego08.essentials.protocollib.component.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.function.Supplier;

/**
 * Represents a text component that can hold various text styles and events.
 */
public final class TextComponent extends AbstractComponent {

    public boolean forceUnItalic;
    private String text;
    private String color;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean obfuscated;
    private String insertion;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;

    /**
     * Constructs a new {@link TextComponent} with the specified parameters.
     *
     * @param text          the text of the component.
     * @param color         the color of the text.
     * @param bold          whether the text is bold.
     * @param italic        whether the text is italic.
     * @param underlined    whether the text is underlined.
     * @param strikethrough whether the text is strikethrough.
     * @param obfuscated    whether the text is obfuscated.
     * @param forceUnItalic whether to force the text to be non-italic.
     * @param insertion     the insertion text.
     * @param clickEvent    the click event.
     * @param hoverEvent    the hover event.
     */
    public TextComponent(String text, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated, boolean forceUnItalic, String insertion, ClickEvent clickEvent, HoverEvent hoverEvent) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.forceUnItalic = forceUnItalic;
        this.insertion = insertion;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
    }

    /**
     * Constructs a new {@link TextComponent} with the specified text.
     *
     * @param text the text of the component.
     */
    public TextComponent(final String text) {
        this.text = text;
    }

    /**
     * Constructs an empty {@link TextComponent}.
     */
    public TextComponent() {
    }

    /**
     * Constructs a new {@link TextComponent} from an existing {@link net.kyori.adventure.text.TextComponent}.
     *
     * @param component the existing {@link net.kyori.adventure.text.TextComponent}.
     */
    public TextComponent(final net.kyori.adventure.text.TextComponent component) {
        this.text = component.content();

        final TextColor color = component.color();
        if (color != null) this.color = color.asHexString();

        this.bold = component.style().hasDecoration(TextDecoration.BOLD);
        this.italic = component.style().hasDecoration(TextDecoration.ITALIC);
        this.underlined = component.style().hasDecoration(TextDecoration.UNDERLINED);
        this.strikethrough = component.style().hasDecoration(TextDecoration.STRIKETHROUGH);
        this.obfuscated = component.style().hasDecoration(TextDecoration.OBFUSCATED);

        this.insertion = component.insertion();

        var clickEvent = component.clickEvent();
        if (clickEvent != null) this.clickEvent = new ClickEvent(clickEvent);

        var hoverEvent = component.hoverEvent();
        if (hoverEvent != null) this.hoverEvent = new HoverEvent(hoverEvent);

        if (!component.children().isEmpty()) {
            for (final Component child : component.children()) {
                this.extra.add(AbstractComponent.parse(child));
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

        appendTag(builder, color, () -> "<" + color + ">");
        appendTag(builder, bold, "<bold>");
        appendTag(builder, italic, "<italic>");
        appendTag(builder, forceUnItalic, "<!italic>");
        appendTag(builder, underlined, "<underlined>");
        appendTag(builder, strikethrough, "<strikethrough>");
        appendTag(builder, obfuscated, "<obfuscated>");
        appendTag(builder, insertion, () -> "<insert:" + insertion + ">");
        appendTag(builder, clickEvent, () -> clickEvent.toMiniMessage());
        appendTag(builder, hoverEvent, () -> hoverEvent.toMiniMessage());
        appendTag(builder, text != null && !text.isEmpty(), text);

        for (AbstractComponent component : extra) {
            builder.append(component.toMiniMessage());
        }

        appendClosingTag(builder, hoverEvent, "</hover>");
        appendClosingTag(builder, clickEvent, "</click>");
        appendClosingTag(builder, insertion, "</insert>");
        appendClosingTag(builder, obfuscated, "</obfuscated>");
        appendClosingTag(builder, strikethrough, "</strikethrough>");
        appendClosingTag(builder, underlined, "</underlined>");
        appendClosingTag(builder, italic, "</italic>");
        appendClosingTag(builder, forceUnItalic, "</!italic>");
        appendClosingTag(builder, bold, "</bold>");
        appendClosingTag(builder, color, "</" + color + ">");

        return builder.toString();
    }

    private void appendTag(StringBuilder builder, Object value, Supplier<String> tagSupplier) {
        if (value != null) {
            builder.append(tagSupplier.get());
        }
    }

    private void appendTag(StringBuilder builder, boolean condition, String tag) {
        if (condition) {
            builder.append(tag);
        }
    }

    private void appendClosingTag(StringBuilder builder, boolean condition, String tag) {
        if (condition) {
            builder.append(tag);
        }
    }

    private void appendClosingTag(StringBuilder builder, Object value, String tag) {
        if (value != null) {
            builder.append(tag);
        }
    }


    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public boolean isObfuscated() {
        return obfuscated;
    }

    public boolean isForceUnItalic() {
        return forceUnItalic;
    }

    public String getInsertion() {
        return insertion;
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }
}