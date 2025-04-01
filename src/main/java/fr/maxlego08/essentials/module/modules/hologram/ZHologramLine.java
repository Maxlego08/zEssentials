package fr.maxlego08.essentials.module.modules.hologram;

import fr.maxlego08.essentials.api.hologram.HologramLine;

public class ZHologramLine implements HologramLine {

    private final String eventName;
    private final boolean isAutoUpdate;
    private int line;
    private String text;


    public ZHologramLine(int line, String text, String eventName, boolean isAutoUpdate) {
        this.line = line;
        this.text = text;
        this.eventName = eventName;
        this.isAutoUpdate = isAutoUpdate;
    }

    public ZHologramLine(int line, String text, boolean isAutoUpdate) {
        this.line = line;
        this.text = text;
        this.eventName = null;
        this.isAutoUpdate = isAutoUpdate;
    }

    @Override
    public boolean isAutoUpdate() {
        return this.isAutoUpdate;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }
}
