package org.rossedth.fsm;
import org.jeasy.states.api.AbstractEvent;

public class AEvent extends AbstractEvent {

    public AEvent() {
        super("AEvent");
    }

    protected AEvent(String name) {
        super(name);
    }

}