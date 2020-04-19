package PCS.PCSCore;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import PCS.PayMachineHandler.PayMachineHandler;
import PCS.Ticket;

import java.lang.reflect.Array;
import java.util.ArrayList;


//======================================================================
// PCSCore
public class PCSCore extends AppThread {
    private final int pollTime;
    private final int PollTimerID = 1;
    private final int openCloseGateTime;        // for demo only!!!
    private final int OpenCloseGateTimerID = 2;        // for demo only!!!
    private MBox gateMBox;
    private MBox paymentMBox;
    private MBox dispatcherMBox;
    private MBox collectorMBox;
    private boolean gateIsClosed = true;        // for demo only!!
    private final ArrayList<Ticket> tickets = new ArrayList<Ticket>();


    //------------------------------------------------------------
    // PCSCore
    public PCSCore(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
        this.pollTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.PollTime"));
        this.openCloseGateTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.OpenCloseGateTime"));        // for demo only!!!
    } // PCSCore


    //------------------------------------------------------------
    // run
    public void run() {
        Thread.currentThread().setName(id);
        Timer.setTimer(id, mbox, pollTime, PollTimerID);
        Timer.setTimer(id, mbox, openCloseGateTime, OpenCloseGateTimerID);    // for demo only!!!
        log.info(id + ": starting...");

        gateMBox = appKickstarter.getThread("GateHandler").getMBox();
        dispatcherMBox = appKickstarter.getThread("DispatcherHandler").getMBox();
        collectorMBox = appKickstarter.getThread("CollectorHandler").getMBox();
        paymentMBox = appKickstarter.getThread("id:PayMachineHandler").getMBox();

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TimesUp:
                    handleTimesUp(msg);
                    break;

                case GateOpenReply:
                    log.info(id + ": Gate is opened.");
                    gateIsClosed = false;
                    break;

                case GateCloseReply:
                    log.info(id + ": Gate is closed.");
                    gateIsClosed = true;
                    break;

                case OpenSignal:
                    log.info(id + ": sending gate open signal to hardware.");
                    break;

                case CloseSignal:
                    log.info(id + ": sending gate close signal to hardware.");
                    break;

                case sendPollSignal:
                    log.info(id + ": poll request received.");
                    break;

                case PayMachineInsertTicket:
                    log.info(id + ": ticket is inserted.");
                    paymentMBox.send(new Msg(id, mbox, Msg.Type.PrintTicketInfo, ""));
                    break;

                case DispatcherPrintTicket:
                    log.info(id + ": ticket is printed.");
                    int length = tickets.size();
                    Ticket new_ticket = new Ticket(length);
                    tickets.add(new_ticket);
                    log.info(id + ": ticket num is " + length);
                    dispatcherMBox.send(new Msg(id, mbox, Msg.Type.DispatcherGetNewTicketID, Integer.toString(length)));
                    break;

                case DispatcherTakeTicket:
                    log.info(id + ": ticket was taken.");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
                    break;

                case CollectorInsertTicket:
                    log.info(id + ": ticket was taken.");
                    checkCollectedTicket(msg.getDetails());
                    break;

                case AdminOpen:
                    log.info(id + ": admin force open.");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
                    break;

                case PollAck:
                    log.info("PollAck: " + msg.getDetails());
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


    //------------------------------------------------------------
    // run
    private void handleTimesUp(Msg msg) {
        log.info("------------------------------------------------------------");
        switch (Timer.getTimesUpMsgTimerId(msg)) {
            case PollTimerID:
                log.info("Poll: " + msg.getDetails());
                gateMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                Timer.setTimer(id, mbox, pollTime, PollTimerID);
                break;

            case OpenCloseGateTimerID:                    // for demo only!!!
                if (gateIsClosed) {
                    log.info(id + ": Open the gate now (for demo only!!!)");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
                } else {
                    log.info(id + ": Close the gate now (for demo only!!!)");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateCloseRequest, ""));
                }
                Timer.setTimer(id, mbox, openCloseGateTime, OpenCloseGateTimerID);
                break;

            default:
                log.severe(id + ": why am I receiving a timeout with timer id " + Timer.getTimesUpMsgTimerId(msg));
                break;
        }
    } // handleTimesUp


    private void checkCollectedTicket(String tID) {
        int ticketID = Integer.parseInt(tID);
        int count = -1;
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId() == ticketID) {
                count = i;
                break;
            }
        }
        if (count == -1) {
            log.info(id + ": Ticket number is wrong.");
            collectorMBox.send(new Msg(id, mbox, Msg.Type.WrongTicketNumber, ""));
            return;
        } else if (tickets.get(count).getFinishPayment()) {
            log.info(id + ": Ticket is valid.");
            collectorMBox.send(new Msg(id, mbox, Msg.Type.PAck, ""));
            log.info(id + ": Open Gate.");
            gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
            log.info(id + ": ticket leave.");
            tickets.get(count).leave();
        } else {
            log.info(id + ": Ticket is not leaving.");
            collectorMBox.send(new Msg(id, mbox, Msg.Type.NAck, ""));
        }
    }

} // PCSCore
