package fr.maxlego08.essentials.api.hologram.configuration;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.TextDisplay;

public class TextHologramConfiguration extends HologramConfiguration {

    private TextColor background;
    private TextDisplay.TextAlignment textAlignment = TextDisplay.TextAlignment.CENTER;
    private boolean textShadow = false;
    private boolean seeThrough = false;

    public TextColor getBackground() {
        return background;
    }

    public TextDisplay.TextAlignment getTextAlignment() {
        return textAlignment;
    }

    public boolean isTextShadow() {
        return textShadow;
    }

    public boolean isSeeThrough() {
        return seeThrough;
    }

    public void setBackground(TextColor background) {
        this.background = background;
    }

    public void setTextAlignment(TextDisplay.TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
    }

    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }
}
