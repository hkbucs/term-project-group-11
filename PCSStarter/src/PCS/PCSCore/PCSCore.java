package PCS.PCSCore;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;
import PCS.Ticket;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//======================================================================
// PCSCore
public class PCSCore extends AppThread {
    private final int pollTime;
    private final int PollTimerID = 1;
    private final int openCloseGateTime;        // for demo only!!!
    private final int OpenCloseGateTimerID = 2;        // for demo only!!!
    private final Map<Integer, Ticket> ticketMap = new HashMap<>();
    private MBox gateMBox;
    private MBox payMachineMBox;
    private MBox dispatcherMBox;
    private MBox collectorMBox;
    private MBox vacancyDispMBox;
    private boolean gateIsClosed = true;        // for demo only!!
    private boolean demo = false;


    //------------------------------------------------------------
    // PCSCore
    public PCSCore(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
        this.pollTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.PollTime"));
        this.openCloseGateTime = Integer.parseInt(appKickstarter.getProperty("PCSCore.OpenCloseGateTime"));        // for demo only!!!
        if(appKickstarter.getProperty("demo").equals("on")){
            this.demo = true;
        }
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
        payMachineMBox = appKickstarter.getThread("PayMachineHandler").getMBox();
        vacancyDispMBox = appKickstarter.getThread("VacancyDispHandler").getMBox();

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TimesUp:
                    handleTimesUp(msg);
                    break;
                /* For message from Gate */
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

                /* For message from PayMachine */
                case PayMachineInsertTicket:
                    log.info(id + ": ticket is inserted.");
                    handlePayMachineTicket(msg.getDetails());
                    payMachineMBox.send(new Msg(id, mbox, Msg.Type.PrintTicketInfo, ""));
                    break;

                case PayMachinePayment:
                    log.info(id + ": payment is made.");
                    handlePayment(msg.getDetails());
                    break;

                /* For message from Dispatcher */
                case DispatcherCreateTicket:
                    log.info(id + ": ready to create a ticket.");
                    handleCreateTicket();
                    break;

                case DispatcherTakeTicket:
                    log.info(id + ": ticket was taken by Dispatcher.");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
                    break;

                /* For message from Collector */
                case CollectorInsertTicket:
                    log.info(id + ": ticket was taken by Collector.");
                    handleValidation(msg.getDetails());
                    break;

                case AdminOpen:
                    log.info(id + ": admin force open.");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
                    break;

                /* For message from Vacancy Display */
                case CarPassThrough:
                    log.info(id + " One car passes through the sensor.");
                    vacancyDispMBox.send(new Msg(id, mbox, Msg.Type.UpdateDisplay, msg.getDetails()));
                    break;

                /**/
                case sendPollSignal:
                    log.info(id + ": poll request received.");
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
    // handleTimesUp
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

    //------------------------------------------------------------
    // handleCreateTicket

    /**
     * A function of handling the creation of a ticket.
     */
    private void handleCreateTicket() {
        Date enterTime = new Date();
        Ticket newTicket = new Ticket(enterTime);
        ticketMap.put(newTicket.getTicketNumber(), newTicket);
        log.info(id + ": " + newTicket.toString() + " is created");
        dispatcherMBox.send(new Msg(id, mbox, Msg.Type.DispatcherGetNewTicketNumber, String.valueOf(newTicket.getTicketNumber())));
    } // handleCreateTicket

    //------------------------------------------------------------
    // handlePayMachineTicket

    /**
     * A function of handling the insertion
     * of the ticket from Pay Machine Emulator.
     *
     * @param ticketNumber The number of identifying the ticket.
     */
    private void handlePayMachineTicket(String ticketNumber) {
        try {
            Ticket ticket = ticketMap.get(Integer.parseInt(ticketNumber));
            // Validate whether the ticket is exist
            if (ticket != null) {
                Date paymentTime = new Date();
                ticket.setPaymentTime(paymentTime);
                log.info(id + ": Ticket[" + ticket.getTicketNumber() + "] the payment time is updated");
                double fee = calculateFee(ticket.getEnterTime(), ticket.getPaymentTime());
                ticket.setFee(fee);
                log.info(id + ": Ticket[" + ticket.getTicketNumber() + "] the parking fee is updated");
                String message = String.valueOf(ticket.getFee());
                payMachineMBox.send(new Msg(id, mbox, Msg.Type.PrintTicketInfo, message));
            } else {
                log.warning(id + ": Ticket[" + ticketNumber + "] does not exist");
                payMachineMBox.send(new Msg(id, mbox, Msg.Type.PayMachineError, ticketNumber));
            }
        } catch (NumberFormatException e) {
            log.warning(id + ": Invalid input. " + e.getMessage());
            payMachineMBox.send(new Msg(id, mbox, Msg.Type.PayMachineError, ticketNumber));
        }
    }// handlePayMachineTicket

    //------------------------------------------------------------
    // calculateFee

    /**
     * A function of calculation of parking fee.
     *
     * @param from The datetime of the car entrance time.
     * @param to   The datetime of making payment.
     * @return The parking fee.
     */
    private double calculateFee(Date from, Date to) {
        double fee = 0;
        // Calculate the time period
        long diff = to.getTime() - from.getTime();
        // Express the time period in different way
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        // Calculate the fee
        if (diffSeconds > 0) {
            if (diffSeconds > 0) {
                diffMinutes += 1;
            }
        }
        if (diffMinutes > 0) {
            if (diffMinutes > 30) {
                diffHours += 1;
            } else {
                fee += 10;
            }
        }
        if (diffHours > 0) {
            fee += 20 * diffHours;
        }
        if (diffDays > 0) {
            fee += 300 * diffDays;
        }
        return fee;
    }// calculateFee

    //------------------------------------------------------------
    // handlePayment

    /**
     * A function of update the payment
     * status of the ticket.
     *
     * @param ticketNumber The number of identifying the ticket.
     */
    private void handlePayment(String ticketNumber) {
        try {
            Ticket ticket = ticketMap.get(Integer.parseInt(ticketNumber));
            // Validate whether the ticket is exist
            if (ticket != null) {
                ticket.setPaid(true);
                log.info(id + ": Ticket[" + ticket.getTicketNumber() + "] the payment status is updated");
                payMachineMBox.send(new Msg(id, mbox, Msg.Type.PayMachineSuccess, ticketNumber));
            } else {
                log.warning(id + ": Ticket[" + ticketNumber + "] does not exist");
                payMachineMBox.send(new Msg(id, mbox, Msg.Type.PayMachineError, ticketNumber));
            }
        } catch (NumberFormatException e) {
            log.warning(id + ": Invalid input. " + e.getMessage());
            payMachineMBox.send(new Msg(id, mbox, Msg.Type.PayMachineError, ticketNumber));
        }
    }// handlePayment

    //------------------------------------------------------------
    // handleValidation

    /**
     * A function of ticket validation.
     *
     * @param ticketNumber The number of identifying the ticket.
     */
    private void handleValidation(String ticketNumber) {
        try {
            Ticket ticket = ticketMap.get(Integer.parseInt(ticketNumber));
            boolean flag = false;
            // Validate whether the ticket is exist
            if (ticket != null) {
                // Validate Start(so far only check the payment)
                // 1. check payment
                if (ticket.getPaid()) {
                    flag = true;
                }
                // Validate finish
                if (flag) {
                    ticket.setValidation(flag);
                    log.info(id + ": Ticket[" + ticket.getTicketNumber() + "] is valid");
                    collectorMBox.send(new Msg(id, mbox, Msg.Type.PAck, ""));
                    log.info(id + ": Open Gate.");
                    gateMBox.send(new Msg(id, mbox, Msg.Type.GateOpenRequest, ""));
                } else {
                    log.info(id + ": Ticket[" + ticket.getTicketNumber() + "] is invalid");
                    collectorMBox.send(new Msg(id, mbox, Msg.Type.NAck, ""));
                }
            } else {
                log.warning(id + ": Ticket[" + ticketNumber + "] does not exist");
                collectorMBox.send(new Msg(id, mbox, Msg.Type.CollectorError, ticketNumber));
            }
        } catch (NumberFormatException e) {
            log.warning(id + ": Invalid input. " + e.getMessage());
            collectorMBox.send(new Msg(id, mbox, Msg.Type.CollectorError, ticketNumber));
        }

    }// handleValidation

} // PCSCore
