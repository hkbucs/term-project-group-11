package PCS;

import AppKickstarter.timer.Timer;

import PCS.PCSCore.PCSCore;
import PCS.GateHandler.GateHandler;
import PCS.GateHandler.Emulator.GateEmulator;
import PCS.CollectorHandler.CollectorHandler;
import PCS.CollectorHandler.Emulator.CollectorEmulator;
import PCS.DispatcherHandler.DispatcherHandler;
import PCS.DispatcherHandler.Emulator.DispatcherEmulator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

//======================================================================
// PCSEmulatorStarter
public class PCSEmulatorStarter extends PCSStarter
{
    //------------------------------------------------------------
    // main
    public static void main(String [] args)
    {
	    new PCSEmulatorStarter().startApp();
    } // main


    //------------------------------------------------------------
    // startHandlers
    @Override
    protected void startHandlers()
    {
        Emulators.pcsEmulatorStarter = this;
        new Emulators().start();
    } // startHandlers


    //------------------------------------------------------------
    // Emulators
    public static class Emulators extends Application
    {
        private static PCSEmulatorStarter pcsEmulatorStarter;

	//----------------------------------------
	// start
        public void start()
        {
            launch();
	    } // start

	//----------------------------------------
	// start
        public void start(Stage primaryStage)
        {
            Timer timer = null;
            PCSCore pcsCore = null;
            GateEmulator gateEmulator = null;
            DispatcherEmulator dispatcherEmulator = null;
            CollectorEmulator collectorEmulator = null;

            // create emulators
            try {
                timer = new Timer("timer", pcsEmulatorStarter);
                pcsCore = new PCSCore("PCSCore", pcsEmulatorStarter);
                gateEmulator = new GateEmulator("GateHandler", pcsEmulatorStarter);

                // start emulator GUIs
                gateEmulator.start();
                dispatcherEmulator.start();
                collectorEmulator.start();
            }
            catch (Exception e)
            {
                System.out.println("Emulators: start failed");
                e.printStackTrace();
                Platform.exit();
            }
            pcsEmulatorStarter.setTimer(timer);
            pcsEmulatorStarter.setPCSCore(pcsCore);
            pcsEmulatorStarter.setGateHandler(gateEmulator);
            pcsEmulatorStarter.setDispatcherHandler(dispatcherEmulator);
            pcsEmulatorStarter.setCollectorHandler(collectorEmulator);

            // start threads
            new Thread(timer).start();
            new Thread(pcsCore).start();
            new Thread(gateEmulator).start();
            new Thread(dispatcherEmulator).start();
            new Thread(collectorEmulator).start();
        } // start
    } // Emulators


    //------------------------------------------------------------
    //  setters
    private void setTimer(Timer timer) {
        this.timer = timer;
    }
    private void setPCSCore(PCSCore pcsCore) {
        this.pcsCore = pcsCore;
    }
    private void setGateHandler(GateHandler gateHandler) {
	    this.gateHandler = gateHandler;
    }

    public void setDispatcherHandler(DispatcherHandler dispatcherHandler) {
        this.dispatcherHandler = dispatcherHandler;
    }

    public void setCollectorHandler(CollectorHandler collectorHandler) {
        this.collectorHandler = collectorHandler;
    };
} // PCSEmulatorStarter
