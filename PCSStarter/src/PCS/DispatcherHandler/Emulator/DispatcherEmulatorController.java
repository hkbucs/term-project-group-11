package PCS.DispatcherHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.util.logging.Logger;

public class DispatcherEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private DispatcherEmulator dispatcherEmulator;
    private MBox dispatcherHandlerMBox;
    public TextArea dispatcherTextArea;
    private int lineNo = 0;

    public void initialize(String id, AppKickstarter appKickstarter,
                           Logger log, DispatcherEmulator dispatcherEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.dispatcherEmulator = dispatcherEmulator;
        this.dispatcherHandlerMBox = appKickstarter.getThread("DispatcherHandler").getMBox();
    }

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        switch (btn.getText()) {
            case "Give Me a Ticket":
                appendTextArea("Ticket Printed");
                dispatcherHandlerMBox.send(new Msg(id, null, Msg.Type.DispatcherPrintTicket, ""));
                break;
            case "Take the Ticket":
                appendTextArea("Ticket Taken");
                dispatcherHandlerMBox.send(new Msg(id, null, Msg.Type.DispatcherTakeTicket, ""));
                break;
            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    }

    public void appendTextArea(String status) {
        Platform.runLater(() -> dispatcherTextArea.appendText(String.format("[%04d] %s\n", ++lineNo, status)));
    }

}
