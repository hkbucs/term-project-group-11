package PCS.DispatcherHandler.Emulator;

import PCS.PCSStarter;
import PCS.DispatcherHandler.DispatcherHandler;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class DispatcherEmulator extends DispatcherHandler {
    private final PCSStarter pcsStarter;
    private final String id;
    private Stage myStage;
    private DispatcherEmulatorController dispatcherEmulatorController;

    public DispatcherEmulator(String id, PCSStarter pcsStarter) {
        super(id, pcsStarter);
        this.pcsStarter = pcsStarter;
        this.id = id;
    }

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "DispatcherEmulator.fxml";
        loader.setLocation(DispatcherEmulator.class.getResource(fxmlName));
        root = loader.load();
        dispatcherEmulatorController = loader.getController();
        dispatcherEmulatorController.initialize(id, pcsStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 420, 470));
        myStage.setTitle("Dispatcher Emulator");
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
                reloadStage("DispatcherEmulatorReceipt.fxml");
                break;
            case "close":
                reloadStage("DispatcherEmulator.fxml");
                break;
            default:
                log.severe(id + ": UnKnown Info" + msg.getDetails());
                break;
        }
    }

    private void reloadStage(String fxmlFName) {
        DispatcherEmulator dispatcherEmulator = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(id + ": loading fxml: " + fxmlFName);
                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(DispatcherEmulator.class.getResource(fxmlFName));
                    root = loader.load();
                    dispatcherEmulatorController = loader.getController();
                    dispatcherEmulatorController.initialize(id, pcsStarter, log, dispatcherEmulator);
                    myStage.setScene(new Scene(root, 420, 470));
                } catch (Exception e) {
                    log.severe(id + ": failed to load " + fxmlFName);
                    e.printStackTrace();
                }
            }
        });
    }
}
