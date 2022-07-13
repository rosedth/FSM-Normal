package org.rossedth.fsm;
import org.jeasy.states.api.AbstractEvent;

class CEvent extends AbstractEvent {

    public CEvent() {
        super("CEvent");
    }

    protected CEvent(String name) {
        super(name);
    }

}