package PCS.VacancyDispHandler.Emulator;

import AppKickstarter.misc.Msg;
import PCS.PCSStarter;
import PCS.VacancyDispHandler.VacancyDispHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class VacancyDispEmulator extends VacancyDispHandler {
    private final String id;
    private final PCSStarter pcsStarter;
    private VacancyDispEmulatorController vacancyDispEmulatorController;
    private Stage myStage;

    /**
     * Constructor for an appThread
     *
     * @param id         name of the appThread
     * @param pcsStarter a reference to our PCSStarter
     */
    public VacancyDispEmulator(String id, PCSStarter pcsStarter) {
        super(id, pcsStarter);
        this.id = id + "Emulator";
        this.pcsStarter = pcsStarter;
    }

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "VacancyDispEmulator.fxml";
        loader.setLocation(VacancyDispEmulator.class.getResource(fxmlName));
        root = loader.load();
        vacancyDispEmulatorController = loader.getController();
        vacancyDispEmulatorController.initialize(id, pcsStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 500, 200));
        myStage.setTitle("Vacancy Display Emulator");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            pcsStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    }

    protected void handleUpdateDisplay(Msg msg) {
        Platform.runLater(() -> {
            log.info(id + ": " + msg.getDetails() + " and update display.");

            switch (msg.getType()) {
                case UpdateDisplay:
                    String[] details = msg.getDetails().split("-");
                    if (details[1].equals("1")){
                        vacancyDispEmulatorController.handleNumberIncrease(details[0]);
                    }
                    if (details[1].equals("2")){
                        vacancyDispEmulatorController.handleNumberDecrease(details[0]);
                    }
                    break;

                default:
                    log.severe(id + ": update cash dispenser display with unknown display type -- " + msg.getDetails());
                    break;
            }
        });
    }

}
