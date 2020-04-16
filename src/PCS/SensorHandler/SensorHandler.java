package PCS.SensorHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
//import PCS.PCSStarter;

public class SensorHandler extends AppThread {

    // SensorHandler
    public SensorHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }

    // run
    public void run() {
        MBox pcss = appKickstarter.getThread("PCSCore").getMBox();
        log.info(id + ": starting...");

        for (boolean quit = false; !quit;) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case CarPassThrough:
                    pcss.send(new Msg(id, mbox, Msg.Type.CarPassThrough, msg.getDetails()));

                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }

        appKickstarter.unregThread(this);
        log.info( id+ ": Total Cars Number is increase" );
    }
}