package PCS.DispatcherHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


public class DispatcherHandler extends AppThread {
    protected final MBox pcsCore;

    public DispatcherHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
        pcsCore = appKickstarter.getThread("PCSCore").getMBox();
    }

    public void run() {
        Thread.currentThread().setName(id);
        log.info(id + ": starting...");
        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();
            log.fine(id + ": message received: [" + msg + "].");
            quit = processMsg(msg);
        }
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    }

    protected boolean processMsg(Msg msg) {
        boolean quit = false;
        switch (msg.getType()) {
            case DispatcherCreateTicket:
                pcsCore.send(new Msg(id, mbox, Msg.Type.DispatcherCreateTicket, msg.getDetails()));
                printTicket();
                break;
            case DispatcherTakeTicket:
                pcsCore.send(new Msg(id, mbox, Msg.Type.DispatcherTakeTicket, msg.getDetails()));
                takeTicket();
                break;
            case DispatcherGetNewTicketNumber:
                handleNewTicket(msg.getDetails());
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
        return quit;
    }

    //Dispatcher prepares the ticket
    protected void printTicket() {
        log.info(id + ": Ticket Printed");
    }

    //Driver collects the ticket from the dispatcher, and enters the parking lot
    protected void takeTicket() {
        log.info(id + ": Ticket was taken and door opened");
    }

    protected void handleNewTicket(String ticketNumber){
        log.info(id + ": New Ticket With Number " + ticketNumber);
    }
}
