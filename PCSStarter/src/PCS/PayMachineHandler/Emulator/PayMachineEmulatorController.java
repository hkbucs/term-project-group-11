package PCS.PayMachineHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import PCS.PCSStarter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.logging.Logger;

public class PayMachineEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private PayMachineEmulator payMachineEmulator;
    private LocalDateTime currentTime;
    private MBox PayMachineMBox;
    private boolean insert;

    public TextField ticketNumField;
    public TextArea ticketReaderTextArea;

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, PayMachineEmulator payMachineEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.insert = false;
        this.payMachineEmulator = payMachineEmulator;
        if (appKickstarter.getThread("PayMachineHandler") != null) {
            this.PayMachineMBox = appKickstarter.getThread("PayMachineHandler").getMBox();
        }
    }

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        //String buTxt = bu.getText();
        //String buID = bu.getId();

        switch (btn.getText()) {
//            case "Check Ticket":
//                ticketNumField.setText(appKickstarter.getProperty("TicketReader.Ticket"));
//                break;

            case "Reset":
                ticketNumField.setText("");
                break;

            case "Insert Ticket":
                if (ticketNumField.getText().length() != 0) {
                    ticketReaderTextArea.appendText("Sending " + ticketNumField.getText() + "\n");
                    PayMachineMBox.send(new Msg(id, PayMachineMBox, Msg.Type.PayMachineInsertTicket, ticketNumField.getText()));
                    insert = true;
                }
                break;

            case "Remove Ticket":
                ticketReaderTextArea.appendText("Removing ticket\n");
                PayMachineMBox.send(new Msg(id, PayMachineMBox, Msg.Type.PayMachineRemoveTicket, ticketNumField.getText()));
                insert = false;
                break;

            case "Pay":
                if ( insert == true) {
                    ticketReaderTextArea.appendText("Pay Successfully\n");
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    this.currentTime = now;
                    String paymentTime = df.format(now);
                    ticketReaderTextArea.appendText("Payment Time: " + paymentTime + "\n");

                    PayMachineMBox.send(new Msg(id, PayMachineMBox, Msg.Type.PayMachinePayment, ticketNumField.getText()));
                }
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    }

    public void appendTextArea(String status) {
        ticketReaderTextArea.appendText(status + "\n");
    } // appendTextArea

}