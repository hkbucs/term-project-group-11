package PCS.DispatcherHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


public class DispatcherHandler extends AppThread {

    public DispatcherHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    public void run() {
        MBox pcsCore = appKickstarter.getThread("PCSCore").getMBox();
        log.info(id + ": starting...");
        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();
            log.fine(id + ": message received: [" + msg + "].");
            switch (msg.getType()) {
                case DispatcherPrintTicket:
                    pcsCore.send(new Msg(id, mbox, Msg.Type.DispatcherPrintTicket, msg.getDetails()));
                    printTicket();
                    break;
                case DispatcherTakeTicket:
                    pcsCore.send(new Msg(id, mbox, Msg.Type.DispatcherTakeTicket, msg.getDetails()));
                    takeTicket();
                    break;
                case Poll:
                    pcsCore.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                    break;
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

    //Dispatcher prepares the ticket
    protected void printTicket() {
        log.info(id + ": Ticket Printed");
    }

    //Driver collects the ticket from the dispatcher, and enters the parking lot
    protected void takeTicket() {
        log.info(id + ": Ticket was taken and door opened");
    }
}
