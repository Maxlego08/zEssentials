package fr.maxlego08.essentials.api.hologram;

/**
 * Represents a single line in a hologram.
 * Each hologram line has an associated line number, text content, and an optional event name.
 */
public interface HologramLine {

    /**
     * Gets the line number of this hologram line.
     *
     * @return the line number
     */
    int getLine();

    /**
     * Gets the text content of this hologram line.
     *
     * @return the text content
     */
    String getText();

    /**
     * Sets the text content of this hologram line.
     *
     * @param text the new text content
     */
    void setText(String text);

    /**
     * Gets the event name associated with this hologram line.
     * The event name can be used to trigger updates or animations.
     *
     * @return the event name, or null if no event is associated
     */
    String getEventName();

    /**
     * Sets the line number for this hologram line.
     *
     * @param line the new line number
     */
    void setLine(int line);
}

