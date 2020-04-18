package PCS;

import PCS.PCSStarter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Ticket {

    private LocalDateTime entranceTime;
    private LocalDateTime exitTime;
    private LocalDateTime paymentTime;
    private double parkingFee;
    private boolean finishPayment;

    public Ticket(){
        LocalDateTime now = LocalDateTime.now();
        this.entranceTime = now;
        this.exitTime = null;
        this.paymentTime = null;
        this.parkingFee = 0;
        this.finishPayment = true;
    }

    public boolean finishPayment(){
        return finishPayment;
    }
}
