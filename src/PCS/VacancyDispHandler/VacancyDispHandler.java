package PCS.VacancyDispHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


public class VacancyDispHandler extends AppThread {


    public VacancyDispHandler(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
    }
    public void run() {
        MBox pcss = appKickstarter.getThread("PCSS").getMBox();
        log.info(id + ": starting...");
    }

    for (boolean quit = false; !quit;) {
        Msg msg = mbox.receive();

        log.fine(id + ": message received: [" + msg + "].");

        switch (msg.getType()) {
            case UpdatedDisplay:
                handleUpdateDisplay(msg);
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    }

    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());
    }

}