package org.rossedth.fsm;
import org.jeasy.states.api.AbstractEvent;

public class BEvent extends AbstractEvent {

    public BEvent() {
        super("BEvent");
    }

    protected BEvent(String name) {
        super(name);
    }

}