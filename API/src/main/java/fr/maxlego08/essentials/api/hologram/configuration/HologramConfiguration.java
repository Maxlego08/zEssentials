package fr.maxlego08.essentials.api.hologram.configuration;

import org.bukkit.entity.Display;
import org.joml.Vector3f;

/**
 * This abstract class represents the configuration settings for a hologram.
 * It includes various properties such as billboard mode, scale, translation,
 * brightness, shadow settings, and visibility distance.
 */
public abstract class HologramConfiguration {

    private Display.Billboard billboard = Display.Billboard.CENTER;
    private Vector3f scale = new Vector3f(1, 1, 1);
    private Vector3f translation = new Vector3f(0, 0, 0);
    private Display.Brightness brightness = new Display.Brightness(15, 15);
    private float shadowRadius = 0.0f;
    private float shadowStrength = 1.0f;
    private int visibilityDistance = -1;

    /**
     * Gets the billboard mode for the hologram, which determines how it faces the viewer.
     *
     * @return the current billboard mode
     */
    public Display.Billboard getBillboard() {
        return billboard;
    }

    /**
     * Sets the billboard mode for the hologram.
     *
     * @param billboard the new billboard mode
     */
    public void setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
    }

    /**
     * Gets the scale of the hologram.
     *
     * @return the current scale as a {@link Vector3f}
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * Sets the scale of the hologram.
     *
     * @param scale the new scale as a {@link Vector3f}
     */
    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    /**
     * Gets the translation offset of the hologram.
     *
     * @return the current translation as a {@link Vector3f}
     */
    public Vector3f getTranslation() {
        return translation;
    }

    /**
     * Sets the translation offset of the hologram.
     *
     * @param translation the new translation as a {@link Vector3f}
     */
    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    /**
     * Gets the brightness settings of the hologram.
     *
     * @return the current brightness as a {@link Display.Brightness} object
     */
    public Display.Brightness getBrightness() {
        return brightness;
    }

    /**
     * Sets the brightness of the hologram.
     *
     * @param brightness the new brightness as a {@link Display.Brightness} object
     */
    public void setBrightness(Display.Brightness brightness) {
        this.brightness = brightness;
    }

    /**
     * Gets the shadow radius of the hologram.
     *
     * @return the current shadow radius
     */
    public float getShadowRadius() {
        return shadowRadius;
    }

    /**
     * Sets the shadow radius of the hologram.
     *
     * @param shadowRadius the new shadow radius
     */
    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    /**
     * Gets the shadow strength of the hologram.
     *
     * @return the current shadow strength
     */
    public float getShadowStrength() {
        return shadowStrength;
    }

    /**
     * Sets the shadow strength of the hologram.
     *
     * @param shadowStrength the new shadow strength
     */
    public void setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
    }

    /**
     * Gets the visibility distance of the hologram, determining how far it can be seen.
     *
     * @return the current visibility distance
     */
    public int getVisibilityDistance() {
        return visibilityDistance;
    }

    /**
     * Sets the visibility distance of the hologram.
     *
     * @param visibilityDistance the new visibility distance
     */
    public void setVisibilityDistance(int visibilityDistance) {
        this.visibilityDistance = visibilityDistance;
    }
}
