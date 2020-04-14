package PCS.DispatcherHandler.Emulator;

import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.*;

public class DispatcherHandler extends AppThread {
    public DispatcherHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    public void run() {

    }

    protected void buttonPressed() {
        log.info(id + ": button pressed");
    }

    protected void prepareTicket() {
        log.info(id + ": Ticket Prepared");
    }

    protected void takeTicket() {
        log.info(id + ": Ticket was taken and door opened");

    }


}
