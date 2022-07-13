package org.rossedth.fsm;
import org.jeasy.states.api.AbstractEvent;

class BEvent extends AbstractEvent {

    public BEvent() {
        super("BEvent");
    }

    protected BEvent(String name) {
        super(name);
    }

}