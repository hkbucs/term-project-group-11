package PCS.PayMachineHandler.Emulator;

import PCS.PCSStarter;
import PCS.PayMachineHandler.PayMachineHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class PayMachineEmulator extends PayMachineHandler {
    private final String id;
    private final PCSStarter pcsStarter;
    private PayMachineEmulatorController payMachineEmulatorController;
    private Stage myStage;

    /**
     * Constructor for an appThread
     *
     * @param id         name of the appThread
     * @param pcsStarter a reference to our PCSStarter
     */
    public PayMachineEmulator(String id, PCSStarter pcsStarter) {
        super(id, pcsStarter);
        this.id = id + "Emulator";
        this.pcsStarter = pcsStarter;
    }

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "PayMachineEmulator.fxml";
        loader.setLocation(PayMachineEmulator.class.getResource(fxmlName));
        root = loader.load();
        payMachineEmulatorController = loader.getController();
        payMachineEmulatorController.initialize(id, pcsStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 470));
        myStage.setTitle("Pay Machine Emulator");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            pcsStarter.stopApp();
            Platform.exit();
        });

        myStage.show();
    }
        protected void handleTicketInsert() {
            super.handleTicketInsert();
            payMachineEmulatorController.appendTextArea("Ticket Inserted");
        } // handleCardInsert

        /**
         * This method is used to show card removed information in text area and update
         * card statues after Card Removed
         */

        // ------------------------------------------------------------
        // handleCardRemove
        protected void handleTicketRemove() {
            super.handleTicketRemove();
            payMachineEmulatorController.appendTextArea("Ticket Removed");
        } // handleCardRemove
    } // CardReaderEmulator

