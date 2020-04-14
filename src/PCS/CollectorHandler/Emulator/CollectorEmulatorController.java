package PCS.CollectorHandler.Emulator;

import java.util.logging.Logger;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class CollectorEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private CollectorEmulator collectorEmulator;
    private MBox collectorHandlerMBox;

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, CollectorEmulator collectorEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.collectorEmulator = collectorEmulator;
        this.collectorHandlerMBox = appKickstarter.getThread("CashDepositCollectorHandler").getMBox();
    }

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        collectorHandlerMBox.send(new Msg(id, collectorHandlerMBox, Msg.Type.CDC_ButtonPressed, btnTxt));
    }

}
