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
                    String[] details = msg.getDetails().split(",");
                    if (Integer.parseInt(details[0]) != 0) {
                        if (Integer.parseInt(details[0]) == 1) {
                            vacancyDispEmulatorController.text1.setText(details[1]);
                        } else if (Integer.parseInt(details[0]) == 2) {
                            vacancyDispEmulatorController.text2.setText(details[1]);
                        } else if (Integer.parseInt(details[0]) == 3) {
                            vacancyDispEmulatorController.text3.setText(details[1]);
                        } else if (Integer.parseInt(details[0]) == 4) {
                            vacancyDispEmulatorController.text4.setText(details[1]);
                        }
                    }
                    break;

                default:
                    log.severe(id + ": update cash dispenser display with unknown display type -- " + msg.getDetails());
                    break;
            }
        });
    }

}
