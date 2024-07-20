package fr.maxlego08.essentials.api.event.events.economy;

import fr.maxlego08.essentials.api.economy.Baltop;
import fr.maxlego08.essentials.api.event.EssentialsEvent;

public class EconomyBaltopUpdateEvent extends EssentialsEvent {

    private final Baltop baltop;

    public EconomyBaltopUpdateEvent(Baltop baltop) {
        this.baltop = baltop;
    }

    public Baltop getBaltop() {
        return baltop;
    }
}
