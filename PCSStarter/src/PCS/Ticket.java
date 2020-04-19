package PCS;

import java.util.Date;

public class Ticket {
    private static int count = 10000000;
    private final int ticketNumber;
    private final Date enterTime;
    private Date leaveTime;
    private Date paymentTime;
    private double fee;
    private boolean validation = false;
    private boolean paid = false;

    public Ticket(Date enterTime) {
        ticketNumber = count++;
        this.enterTime = enterTime;
        leaveTime = null;
        paymentTime = null;
        fee = 0;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean getValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public String toString() {
        return "Ticket{" +
                "ticketNumber=" + ticketNumber +
                ", enterTime=" + enterTime +
                ", leaveTime=" + leaveTime +
                ", paymentTime=" + paymentTime +
                ", validation=" + validation +
                '}';
    }
}
