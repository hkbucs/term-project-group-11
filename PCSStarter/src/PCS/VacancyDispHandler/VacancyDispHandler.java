package PCS.VacancyDispHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

public class VacancyDispHandler extends AppThread {

    /**
     * Constructor for an appThread
     *
     * @param id             name of the appThread
     * @param appKickstarter a reference to our AppKickstarter
     */
    public VacancyDispHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    public void run() {
        log.info(id + ": starting...");


        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case UpdateDisplay:
                    handleUpdateDisplay(msg);
                    break;

                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
            appKickstarter.unregThread(this);
            log.info(id + ": terminating...");
        }
    }

    protected void handleUpdateDisplay(Msg msg) {
//        String[] msgDetail = msg.getDetails().split("-");
//        log.info(id + ": update display -- " + msgDetail[0] + "floor" + (50 - Integer.parseInt(msgDetail[1])) + "places left.");
    }
}
