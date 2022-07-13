package org.rossedth.fsm;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.EventHandler;

class Recognized implements EventHandler<AbstractEvent> {

    public void handleEvent(AbstractEvent event) {
        System.out.println("Recognized sequence.");
    }

}