package fr.maxlego08.essentials.api.hologram.configuration;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.TextDisplay;

/**
 * This class represents the configuration settings for a text hologram.
 * It extends the {@link HologramConfiguration} class to include additional properties
 * specific to text holograms, such as background color, text alignment, text shadow, and see-through options.
 */
public class TextHologramConfiguration extends HologramConfiguration {

    private TextColor background;
    private TextDisplay.TextAlignment textAlignment = TextDisplay.TextAlignment.CENTER;
    private boolean textShadow = false;
    private boolean seeThrough = false;

    /**
     * Gets the background {@link TextColor} of the text hologram.
     *
     * @return the background color
     */
    public TextColor getBackground() {
        return background;
    }

    /**
     * Gets the {@link TextDisplay.TextAlignment} for the text hologram.
     *
     * @return the text alignment
     */
    public TextDisplay.TextAlignment getTextAlignment() {
        return textAlignment;
    }

    /**
     * Checks if the text shadow is enabled for the text hologram.
     *
     * @return true if text shadow is enabled, false otherwise
     */
    public boolean isTextShadow() {
        return textShadow;
    }

    /**
     * Checks if the text hologram is set to be see-through.
     *
     * @return true if the hologram is see-through, false otherwise
     */
    public boolean isSeeThrough() {
        return seeThrough;
    }

    /**
     * Sets the background {@link TextColor} for the text hologram.
     *
     * @param background the background color to set
     */
    public void setBackground(TextColor background) {
        this.background = background;
    }

    /**
     * Sets the {@link TextDisplay.TextAlignment} for the text hologram.
     *
     * @param textAlignment the text alignment to set
     */
    public void setTextAlignment(TextDisplay.TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    /**
     * Enables or disables the text shadow for the text hologram.
     *
     * @param textShadow true to enable text shadow, false to disable
     */
    public void setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
    }

    /**
     * Sets whether the text hologram should be see-through.
     *
     * @param seeThrough true to make the hologram see-through, false otherwise
     */
    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }
}
