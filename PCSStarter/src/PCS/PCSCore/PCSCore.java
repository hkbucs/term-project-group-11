package PCS.PCSCore;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import PCS.PayMachineHandler.PayMachineHandler;
import PCS.Ticket;

import java.util.Hashtable;


//======================================================================
// PCSCore
public class PCSCore extends AppThread {
    private MBox gateMBox;
	private MBox paymentMBox;

    private final int pollTime;
    private final int PollTimerID=1;
    private final int openCloseGateTime;		// for demo only!!!
    private final int OpenCloseGateTimerID=2;		// for demo only!!!
    private boolean gateIsClosed = true;		// for demo only!!
	private Hashtable<Integer, Ticket> Tickets;


    //------------------------------------------------------------
    // PCSCore
    public PCSCore(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
	this.pollTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.PollTime"));
	this.openCloseGateTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.OpenCloseGateTime"));		// for demo only!!!
		this.Tickets = new Hashtable<Integer, Ticket>();
    } // PCSCore


    //------------------------------------------------------------
    // run
    public void run() {
        Thread.currentThread().setName(id);
	Timer.setTimer(id, mbox, pollTime, PollTimerID);
	Timer.setTimer(id, mbox, openCloseGateTime, OpenCloseGateTimerID);	// for demo only!!!
	log.info(id + ": starting...");

	gateMBox = appKickstarter.getThread("GateHandler").getMBox();
	paymentMBox = appKickstarter.getThread("id:PayMachineHandler").getMBox();

	for (boolean quit = false; !quit;) {
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

	    case OpenCloseGateTimerID:					// for demo only!!!
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
} // PCSCore
