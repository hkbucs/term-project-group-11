package PCS.CollectorHandler.Emulator;

import PCS.DispatcherHandler.Emulator.DispatcherEmulator;
import PCS.DispatcherHandler.Emulator.DispatcherEmulatorController;
import PCS.PCSStarter;
import PCS.CollectorHandler.CollectorHandler;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class CollectorEmulator extends CollectorHandler {
    private final PCSStarter pcsStarter;
    private final String id;
    private Stage myStage;
    private CollectorEmulatorController collectorEmulatorController;

    public CollectorEmulator(String id, PCSStarter pcsStarter) {
        super(id, pcsStarter);
        this.pcsStarter = pcsStarter;
        this.id = id;
    }

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "CollectorEmulator.fxml";
        loader.setLocation(CollectorEmulator.class.getResource(fxmlName));
        root = loader.load();
        collectorEmulatorController = loader.getController();
        collectorEmulatorController.initialize(id, pcsStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 420, 470));
        myStage.setTitle("Collector Emulator");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            pcsStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    }

    protected void printTicket(Msg msg) {
        log.info(id + ": " + msg.getDetails());
        switch (msg.getDetails()) {
            case "print":
                reloadStage("CollectorEmulatorReceipt.fxml");
                break;
            case "close":
                reloadStage("CollectorEmulator.fxml");
                break;
            default:
                log.severe(id + ": UnKnown Info" + msg.getDetails());
                break;
        }
    }

    private void reloadStage(String fxmlFName) {
        CollectorEmulator collectorEmulator = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(id + ": loading fxml: " + fxmlFName);
                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(DispatcherEmulator.class.getResource(fxmlFName));
                    root = loader.load();
                    collectorEmulatorController = loader.getController();
                    collectorEmulatorController.initialize(id, pcsStarter, log, collectorEmulator);
                    myStage.setScene(new Scene(root, 420, 470));
                } catch (Exception e) {
                    log.severe(id + ": failed to load " + fxmlFName);
                    e.printStackTrace();
                }
            }
        });
    }
}
