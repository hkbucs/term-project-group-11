package PCS.PayMachineHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

public class PayMachineHandler extends AppThread {

    /**
     * Constructor for an appThread
     *
     * @param id             name of the appThread
     * @param appKickstarter a reference to our AppKickstarter
     */
    public PayMachineHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    // run
    public void run() {
        MBox pcsCore = appKickstarter.getThread("PCSCore").getMBox();
        log.info(id + ": starting...");

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case PayMachineInsertTicket:
                    pcsCore.send(new Msg(id, mbox, Msg.Type.PayMachineInsertTicket, msg.getDetails()));
                    handleTicketInsert();
                    break;
                //payment request
                case PayMachineRemoveTicket:
//                    pcsCore.send(new Msg(id, mbox, Msg.Type.PayMachineRemoveTicket, msg.getDetails()));
                    handleTicketRemove();
                    break;

                case PrintTicketInfo:
                    handlePrintTicketInfo(msg.getDetails());
                    break;

                case PayMachinePayment:
                    log.info(id + ": Processing payment for ticket: " + msg.getDetails());
                    pcsCore.send(new Msg(id, mbox, Msg.Type.PayMachinePayment, msg.getDetails()));
                    break;

                case PayMachineError:
                    log.warning(id + "The ticket with number " + msg.getDetails() + "does not exist");
                    // fixme: handle the error message.
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

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run

    /**
     * This method is used to show the information when card is inserted
     */

    // ------------------------------------------------------------
    // handleCardInsert
    protected void handleTicketInsert() {
        log.info(id + ": ticket inserted");
    } // handleCardInsert

    /**
     * This method is used to show the information when card is inserted
     */

    // ------------------------------------------------------------
    // handleCardInsert
    protected void handleTicketRemove() {
        log.info(id + ": ticket removed");
    } // handleCardInsert

    protected void handlePrintTicketInfo(String ticketinfo) {
        log.info(id + "Entrance time " + ticketinfo);
    } // handlePrintTicketInfo

}
