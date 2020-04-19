package PCS.VacancyDispHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class VacancyDispEmulatorController {

    @FXML
    TextField floor1;
    @FXML
    TextField floor2;
    @FXML
    TextField floor3;
    @FXML
    TextField floor4;
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private VacancyDispEmulator vacancyDispEmulator;
    private MBox vacancyDispMBox;

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, VacancyDispEmulator vacancyDispEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.vacancyDispEmulator = vacancyDispEmulator;
        if (appKickstarter.getThread("VacancyDispHandler") != null) {
            this.vacancyDispMBox = appKickstarter.getThread("VacancyDispHandler").getMBox();
        }
        floor1.setText(this.appKickstarter.getProperty("Park.floorQuota"));
        floor2.setText(this.appKickstarter.getProperty("Park.floorQuota"));
        floor3.setText(this.appKickstarter.getProperty("Park.floorQuota"));
        floor4.setText(this.appKickstarter.getProperty("Park.floorQuota"));
    }

    protected void handleNumberIncrease(String currentFloor){
        switch (currentFloor){
            case "floor1":
                floor1.setText(String.valueOf(Integer.parseInt(floor1.getText())+1));
                break;
            case "floor2":
                floor2.setText(String.valueOf(Integer.parseInt(floor2.getText())+1));
                break;
            case "floor3":
                floor3.setText(String.valueOf(Integer.parseInt(floor3.getText())+1));
                break;
            case "floor4":
                floor4.setText(String.valueOf(Integer.parseInt(floor4.getText())+1));
                break;
        }
    }

    protected void handleNumberDecrease(String currentFloor){
        switch (currentFloor){
            case "floor1":
                floor1.setText(String.valueOf(Integer.parseInt(floor1.getText())-1));
                break;
            case "floor2":
                floor2.setText(String.valueOf(Integer.parseInt(floor2.getText())-1));
                break;
            case "floor3":
                floor3.setText(String.valueOf(Integer.parseInt(floor3.getText())-1));
                break;
            case "floor4":
                floor4.setText(String.valueOf(Integer.parseInt(floor4.getText())-1));
                break;
        }
    }

}
