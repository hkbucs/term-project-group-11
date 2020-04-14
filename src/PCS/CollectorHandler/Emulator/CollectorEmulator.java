package PCS.CollectorHandler.Emulator;

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

public class CollectorEmulator extends CollectorHandler
{
    private PCSStarter pcsStarter;
    private String id;
    private Stage myStage;
    private CollectorEmulatorController collectorEmulatorController;

    public CollectorEmulator(String id, PCSStarter pcsStarter)
    {
        super(id, pcsStarter);
        this.pcsStarter = pcsStarter;
        this.id = id;
    }

    public void start() throws Exception
    {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "CollectorEmulator.fxml";
        loader.setLocation(CollectorEmulator.class.getResource(fxmlName));
        root = loader.load();
        collectorEmulatorController = (CollectorEmulatorController) loader.getController();
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
}
