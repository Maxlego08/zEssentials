package fr.maxlego08.essentials.api.hologram.configuration;

import org.bukkit.entity.Display;
import org.joml.Vector3f;

public abstract class HologramConfiguration {

    private Display.Billboard billboard = Display.Billboard.CENTER;
    private Vector3f scale = new Vector3f(1, 1, 1);
    private Vector3f translation = new Vector3f(0, 0, 0);
    private Display.Brightness brightness = new Display.Brightness(15, 15);
    private float shadowRadius = 0.0f;
    private float shadowStrength = 1.0f;
    private int visibilityDistance = -1;

    public Display.Billboard getBillboard() {
        return billboard;
    }

    public void setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public Display.Brightness getBrightness() {
        return brightness;
    }

    public void setBrightness(Display.Brightness brightness) {
        this.brightness = brightness;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public float getShadowStrength() {
        return shadowStrength;
    }

    public void setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
    }

    public int getVisibilityDistance() {
        return visibilityDistance;
    }

    public void setVisibilityDistance(int visibilityDistance) {
        this.visibilityDistance = visibilityDistance;
    }
}
