package fr.maxlego08.essentials.api.hologram;

/**
 * Represents a single line in a hologram.
 * Each hologram line has an associated line number, text content, and an optional event name.
 */
public class HologramLine {

    private final String eventName;
    private final boolean isAutoUpdate;
    private int line;
    private String text;

    public HologramLine(int line, String text, String eventName, boolean isAutoUpdate) {
        this.line = line;
        this.text = text;
        this.eventName = eventName;
        this.isAutoUpdate = isAutoUpdate;
    }

    public HologramLine(int line, String text, boolean isAutoUpdate) {
        this.line = line;
        this.text = text;
        this.eventName = null;
        this.isAutoUpdate = isAutoUpdate;
    }

    /**
     * Gets the line number of this hologram line.
     *
     * @return the line number
     */
    public int getLine() {
        return this.line;
    }

    /**
     * Sets the line number for this hologram line.
     *
     * @param line the new line number
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * Gets the text content of this hologram line.
     *
     * @return the text content
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text content of this hologram line.
     *
     * @param text the new text content
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the event name associated with this hologram line.
     * The event name can be used to trigger updates or animations.
     *
     * @return the event name, or null if no event is associated
     */
    public String getEventName() {
        return this.eventName;
    }

    ;

    /**
     * Determines if this hologram line is auto-updated.
     *
     * @return true if this hologram line is auto-updated, false otherwise
     */
    public boolean isAutoUpdate() {
        return this.isAutoUpdate;
    }
}

