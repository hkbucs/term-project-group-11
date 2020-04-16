package PCS.PCSCore;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;


//======================================================================
// PCSCore
public class PCSCore extends AppThread {
    private MBox gateMBox;
    private MBox sensorMBox;
    private MBox vacancyDispMBox;

    private final int pollTime;
    private final int PollTimerID=1;
    //private final int openCloseGateTime;		// for demo only!!!
    //private final int OpenCloseGateTimerID=2;		// for demo only!!!
    private boolean gateIsClosed = true;		// for demo only!!!
	private int[] CarsNumber;

    //------------------------------------------------------------
    // PCSCore
    public PCSCore(String id, AppKickstarter appKickstarter) throws Exception {
		super(id, appKickstarter);
		this.pollTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.PollTime"));
		//this.openCloseGateTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.OpenCloseGateTime"));		// for demo only!!!
		CarsNumber = new int[]{30, 30, 30, 30}; //total 50 each
    } // PCSCore


    //------------------------------------------------------------
    // run
    public void run() {
        Thread.currentThread().setName(id);
		Timer.setTimer(id, mbox, pollTime, PollTimerID);
		//Timer.setTimer(id, mbox, openCloseGateTime, OpenCloseGateTimerID);	// for demo only!!!
		log.info(id + ": starting...");

		gateMBox = appKickstarter.getThread("GateHandler").getMBox();
		sensorMBox = appKickstarter.getThread("SensorHandler").getMBox();
		vacancyDispMBox = appKickstarter.getThread("VacancyDispHandler").getMBox();

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			log.fine(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
			case TimesUp:
				handleTimesUp(msg);
				Timer.cancelTimer(id, mbox, pollTime);
				break;

			case GateOpenReply:
				log.info(id + ": Gate is opened.");
				gateIsClosed = false;
				break;

			case GateCloseReply:
				log.info(id + ": Gate is closed.");
				gateIsClosed = true;
				break;

			case PollAck:
				log.info("PollAck: " + msg.getDetails());
				break;

			case Terminate:
				quit = true;
				break;

			case CarPassThrough:
				log.info(msg.getDetails().substring(0,1)+"floor car increase");
				int floorN = Integer.parseInt(msg.getDetails().substring(0,1));
				if (floorN!= 0){
					int num = CarsNumber[floorN-1]+1;
					if(num<=50) {
						CarsNumber[floorN-1]++;
						vacancyDispMBox.send(new Msg(id, mbox, Msg.Type.UpdatedDisplay,
								msg.getDetails().substring(0,1)+","+CarsNumber[floorN-1]));
					}else{
						vacancyDispMBox.send(new Msg(id, mbox, Msg.Type.CannotUpdate,
								"No Car to Leave"));
					}
				}
				break;

			case CarLeave:
				log.info(msg.getDetails().substring(0,1)+"floor car decrease");
				floorN = Integer.parseInt(msg.getDetails().substring(0,1));
				if (floorN!= 0) {
					int num = CarsNumber[floorN-1]-1;
					if (num >= 0) {
						CarsNumber[floorN-1]--;
						vacancyDispMBox.send(new Msg(id, mbox, Msg.Type.UpdatedDisplay,
								msg.getDetails().substring(0, 1) + "," + CarsNumber[floorN-1]));
					} else {
						vacancyDispMBox.send(new Msg(id, mbox, Msg.Type.CannotUpdate,
								"No Place exist"));
					}
				}
				break;

			case CannotUpdate:
				log.info(msg.getDetails().substring(0,1)+"Cannot updated");
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
		sensorMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
		Timer.setTimer(id, mbox, pollTime, PollTimerID);
	        break;

	    //*case OpenCloseGateTimerID:					// for demo only!!!
	     //   if (gateIsClosed) {
			//   log.info(id + ": Open the gate now (for demo only!!!)");
		    //gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
		//} else {
		 //   log.info(id + ": Close the gate now (for demo only!!!)");
		 //   gateMBox.send(new Msg(id, mbox, Msg.Type.GateCloseRequest, ""));
		//}
		//Timer.setTimer(id, mbox, openCloseGateTime, OpenCloseGateTimerID);
		//break;

	    default:
	        log.severe(id + ": why am I receiving a timeout with timer id " + Timer.getTimesUpMsgTimerId(msg));
	        break;
	}
    } // handleTimesUp
} // PCSCore
