package fr.maxlego08.essentials.hologram;

import fr.maxlego08.essentials.api.hologram.HologramLine;

public class ZHologramLine implements HologramLine {

    private final int line;
    private final String text;
    private final String eventName;


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
    public int getLine() {
        return this.line;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }
}
