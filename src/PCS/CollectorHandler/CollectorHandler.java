package PCS.CollectorHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.*;

public class CollectorHandler extends AppThread {
    public CollectorHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    public void run() {
        MBox pcsCore = appKickstarter.getThread("PCSCore").getMBox();
        log.info(id + ": starting...");
        for (boolean quit = false; !quit;) {
            switch (msg.getType()) {
                case Terminate:
                    quit = true;
                    break;
                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    }

    //Press the button on the ticket dispatcher
    protected void buttonPressed() {
        log.info(id + ": button pressed");
    }

    //Dispatcher prepares the ticket
    protected void prepareTicket() {
        log.info(id + ": Ticket Prepared");
    }

    //Driver collects the ticket from the dispatcher, and enters the parking lot
    protected void takeTicket() {
        log.info(id + ": Ticket was taken and door opened");

    }
}
