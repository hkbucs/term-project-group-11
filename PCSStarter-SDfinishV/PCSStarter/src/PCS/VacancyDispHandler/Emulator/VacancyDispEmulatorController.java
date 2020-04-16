package PCS.  VacancyDispHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;


public class VacancyDispEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private VacancyDispEmulator vacancyDispEmulator;
    private MBox vacancyDispMBox;
    @FXML
    TextField text1;
    @FXML
    TextField text2;
    @FXML
    TextField text3;
    @FXML
    TextField text4;

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, VacancyDispEmulator vacancyDispEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.vacancyDispEmulator = vacancyDispEmulator;
        if(appKickstarter.getThread("VacancyDispHandler")!=null) {
            this.vacancyDispMBox = appKickstarter.getThread("VacancyDispHandler").getMBox();
        }
        text1.setText("30");
        text2.setText("30");
        text3.setText("30");
        text4.setText("30");
    }

}