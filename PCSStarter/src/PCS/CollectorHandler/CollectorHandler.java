package PCS.CollectorHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


public class CollectorHandler extends AppThread {
    public CollectorHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    public void run() {
        MBox pcsCore = appKickstarter.getThread("PCSCore").getMBox();
        log.info(id + ": starting...");
        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();
            log.fine(id + ": message received: [" + msg + "].");
            switch (msg.getType()) {
                case CollectorInsertTicket:
                    pcsCore.send(new Msg(id, mbox, Msg.Type.CollectorInsertTicket, msg.getDetails()));
                    insertedTicket();
                    break;
                case AdminOpen:
                    pcsCore.send(new Msg(id, mbox, Msg.Type.AdminOpen, id + " is up!"));
                    adminOpen();
                    break;
                case PAck:
                    pack();
                    break;
                case NAck:
                    nack();
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

    protected void insertedTicket() {
        log.info(id + "Ticket Inserted.");
    }

    protected void adminOpen() {
        log.info(id + ": Admin Pressed the Button");
    }

    protected void pack() {
        log.info(id + ": positive acknowledgement received");
    }

    protected void nack() {
        log.info(id + ": Alert! Admin! ");
    }

}
