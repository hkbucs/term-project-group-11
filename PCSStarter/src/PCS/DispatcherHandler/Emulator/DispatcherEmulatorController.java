package PCS.DispatcherHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.util.logging.Logger;

public class DispatcherEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private DispatcherEmulator dispatcherEmulator;
    private MBox dispatcherEmulatorMBox;

    public void initialize(String id, AppKickstarter appKickstarter,
                           Logger log, DispatcherEmulator dispatcherEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.dispatcherEmulator = dispatcherEmulator;
        this.dispatcherEmulatorMBox = appKickstarter.getThread("DispatcherHandler").getMBox();
    }

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        dispatcherEmulatorMBox.send(new Msg(id, dispatcherEmulatorMBox, Msg.Type.DispatcherPrintTicket, btnTxt));
    }
}
