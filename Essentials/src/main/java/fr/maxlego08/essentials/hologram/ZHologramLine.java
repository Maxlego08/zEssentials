package fr.maxlego08.essentials.hologram;

import fr.maxlego08.essentials.api.hologram.HologramLine;

public class ZHologramLine implements HologramLine {

    private int line;
    private final String eventName;
    private String text;


    public ZHologramLine(int line, String text, String eventName) {
        this.line = line;
        this.text = text;
        this.eventName = eventName;
    }

    public ZHologramLine(int line, String text) {
        this.line = line;
        this.text = text;
        this.eventName = null;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
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
