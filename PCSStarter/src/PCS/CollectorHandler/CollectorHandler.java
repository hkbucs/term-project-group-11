package PCS.CollectorHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


public class CollectorHandler extends AppThread {
    private CollectorStatus status;
    private boolean alarm;
    protected final MBox pcsCore;

    public CollectorHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
        pcsCore = appKickstarter.getThread("PCSCore").getMBox();
        status = CollectorStatus.Idle;
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

    protected boolean processMsg(Msg msg){
        boolean quit = false;
        switch (msg.getType()) {
            case CollectorInsertTicket:
                insertedTicket(msg.getDetails());
                break;
            case AdminOpen:
                adminOpen();
                break;
            case PAck:
                pack();
                break;
            case NAck:
                nack();
                break;
            case CollectorError:
                wrongTicket();
                break;
            case Terminate:
                quit = true;
                break;
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
        return quit;
    }

    protected void insertedTicket(String tID) {
        log.info(id + "Ticket Inserted.");
        switch (status) {
            case Idle:
                log.info(id + ": Sending ticket information to PCS");
                pcsCore.send(new Msg(id, mbox, Msg.Type.CollectorInsertTicket, tID));
                status = CollectorStatus.CollectorInsertTicket;
                break;
            default:
                log.warning(id + ": Ignored");
        }
    }

    protected void adminOpen() {
        log.info(id + ": Admin Pressed the Button");
        pcsCore.send(new Msg(id, mbox, Msg.Type.AdminOpen, ""));
        switch (status)
        {
            case RingingAlarm:
                log.info(id + ": Admin stopped alarm and opened the door");
                alarm = false;
                alarmSignal(alarm);
                status = CollectorStatus.Idle;
                break;
            default:
                log.warning(id + ": Ignored");
        }
    }

    protected void pack() {
        log.info(id + ": positive acknowledgement received");
        switch (status) {
            case CollectorInsertTicket:
                log.info(id + ": valid ticket.");
                status = CollectorStatus.Idle;
                break;
            case RingingAlarm:
                log.warning(id + ": Collector is ringing alarm now!!");
                break;
            default:
                log.warning(id + ": Ignored");
        }
    }

    protected void nack() {
        log.info(id + ": Alert! Admin! ");
        switch (status) {
            case CollectorInsertTicket:
                log.info(id + ": invalid ticket.");
                alarm = true;
                alarmSignal(alarm);
                status = CollectorStatus.RingingAlarm;
                break;
            default:
                log.warning(id + ": Ignored");
        }
    }

    protected void alarmSignal(boolean alarmSignal) {
        if(alarmSignal){
            log.info(id + ": Ring the Alarm");
        }else{
            log.info(id + ": Stop the Alarm");
        }
    }

    protected void wrongTicket(){
        switch (status) {
            case Idle:
                log.info(id + ": Collector idle now.");
                break;
            case CollectorInsertTicket:
                log.warning(id + ": Wrong Ticket");
                status = CollectorStatus.Idle;
                break;
            default:
                log.warning(id + ": Ignored");
        }
    }

    private enum CollectorStatus {
        Idle,
        CollectorInsertTicket,
        RingingAlarm,
    }
}
