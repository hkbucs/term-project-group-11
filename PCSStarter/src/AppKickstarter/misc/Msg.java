package AppKickstarter.misc;


//======================================================================
// Msg
public class Msg {
    private final String sender;
    private final MBox senderMBox;
    private final Type type;
    private final String details;

    //------------------------------------------------------------
    // Msg

    /**
     * Constructor for a msg.
     *
     * @param sender     id of the msg sender (String)
     * @param senderMBox mbox of the msg sender
     * @param type       message type
     * @param details    details of the msg (free format String)
     */
    public Msg(String sender, MBox senderMBox, Type type, String details) {
        this.sender = sender;
        this.senderMBox = senderMBox;
        this.type = type;
        this.details = details;
    } // Msg


    //------------------------------------------------------------
    // getSender

    /**
     * Returns the id of the msg sender
     *
     * @return the id of the msg sender
     */
    public String getSender() {
        return sender;
    }


    //------------------------------------------------------------
    // getSenderMBox

    /**
     * Returns the mbox of the msg sender
     *
     * @return the mbox of the msg sender
     */
    public MBox getSenderMBox() {
        return senderMBox;
    }


    //------------------------------------------------------------
    // getType

    /**
     * Returns the message type
     *
     * @return the message type
     */
    public Type getType() {
        return type;
    }


    //------------------------------------------------------------
    // getDetails

    /**
     * Returns the details of the msg
     *
     * @return the details of the msg
     */
    public String getDetails() {
        return details;
    }


    //------------------------------------------------------------
    // toString

    /**
     * Returns the msg as a formatted String
     *
     * @return the msg as a formatted String
     */
    public String toString() {
        return sender + " (" + type + ") -- " + details;
    } // toString


    //------------------------------------------------------------
    // Msg Types

    /**
     * Message Types used in Msg.
     *
     * @see Msg
     */
    public enum Type {
        /**
         * Terminate the running thread
         */
        Terminate,
        /**
         * Generic error msg
         */
        Error,
        /**
         * Set a timer
         */
        SetTimer,
        /**
         * Set a timer
         */
        CancelTimer,
        /**
         * Timer clock ticks
         */
        Tick,
        /**
         * Time's up for the timer
         */
        TimesUp,
        /**
         * Health poll
         */
        Poll,
        /**
         * Health poll acknowledgement
         */
        PollAck,
        /**
         * Gate open request
         */
        GateOpenRequest,
        /**
         * Gate close request
         */
        GateCloseRequest,
        /**
         * Gate open reply
         */
        GateOpenReply,
        /**
         * Gate close reply
         */
        GateCloseReply,
        /**
         * Toggling Gate Emulator AutoOpen
         */
        GateEmulatorAutoOpenToggle,
        /**
         * Toggling Gate Emulator AutoClose
         */
        GateEmulatorAutoCloseToggle,
        /**
         * Toggling Gate Emulator AutoPoll
         */
        GateEmulatorAutoPollToggle,
        /**
         * Insert a ticket to Collector Emulator
         */
        CollectorInsertTicket,
        /**
         * Admin press the open gate button
         */
        AdminOpen,
        /**
         * Positive Acknowledgement
         */
        PAck,
        /**
         * Negative Acknowledgement
         */
        NAck,
        /**
         * Print the ticket from Dispatcher Emulator
         */
        DispatcherPrintTicket,
        /**
         * Take the ticket from Dispatcher Emulator
         */
        DispatcherTakeTicket,
        /**
         * Insert a ticket to PayMachine Emulator
         */
        PayMachineInsertTicket,
        /**
         * Remove a ticket to PayMachine Emulator
         */
        PayMachineRemoveTicket,
        /**
         * Car passes through Sensor Emulator
         */
        CarPassThrough,
        /**
         * Car leaves Sensor Emulator
         */
        CarLeave,
        /**
         * Update display info on Vacancy Display Emulator
         */
        UpdateDisplay,

    } // Type
} // Msg
