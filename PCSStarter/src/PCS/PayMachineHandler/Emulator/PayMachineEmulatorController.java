package PCS.PayMachineHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import PCS.PCSStarter;

import java.util.logging.Logger;

public class PayMachineEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private PayMachineEmulator payMachineEmulator;
    private MBox gateMBox;

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, PayMachineEmulator payMachineEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.payMachineEmulator = payMachineEmulator;
        this.gateMBox = appKickstarter.getThread("PayMachineHandler").getMBox();
    }
}
