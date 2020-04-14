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

public class DispatcherEmulator extends DispatcherHandler
{
    private PCSStarter pcsStarter;
    private String id;
    private Stage myStage;
    private DispatcherEmulatorController dispatcherEmulatorController;

    public DispatcherEmulator(String id, PCSStarter pcsStarter)
    {
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
        dispatcherEmulatorController = (DispatcherEmulatorController) loader.getController();
        dispatcherEmulatorController.initialize(id, pcsStarter, log, this);
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
}
