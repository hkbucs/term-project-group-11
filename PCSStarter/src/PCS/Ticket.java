package PCS;

import PCS.PCSStarter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Ticket {

    private int id;
    private LocalDateTime entranceTime;
    private LocalDateTime exitTime;
    private boolean finishPayment;
    private boolean inside;

    public Ticket(int id){
        this.id = id;
        LocalDateTime now = LocalDateTime.now();
        this.entranceTime = now;
        this.exitTime = null;
        this.finishPayment = false;
        this.inside = true;
    }

    public void finishPayment(){
        finishPayment = true;
    }

    public int getId(){
        return id;
    }

    public void leave(){
        inside = false;
    }

    public boolean getFinishPayment(){
        return finishPayment;
    }

    public boolean getInside(){
        return inside;
    }

}
