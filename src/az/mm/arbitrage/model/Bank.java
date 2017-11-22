package az.mm.arbitrage.model;

import java.time.LocalDate;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class Bank {

    private int id;
    private String name;
    private double bUSD;
    private double sUSD;
    private double bEUR;
    private double sEUR;
    private double bRUB;
    private double sRUB;
    private double bTRY;
    private double sTRY;
    private double bGBP;
    private double sGBP;
    private LocalDate date;

    public Bank() {
    }

    public Bank(String name, double bUSD, double sUSD, double bEUR, double sEUR, double bRUB, double sRUB, double bGBP, double sGBP, double bTRY, double sTRY) {
        this.name = name;
        this.bUSD = bUSD;
        this.sUSD = sUSD;
        this.bEUR = bEUR;
        this.sEUR = sEUR;
        this.bRUB = bRUB;
        this.sRUB = sRUB;
        this.bGBP = bGBP;
        this.sGBP = sGBP;
        this.bTRY = bTRY;
        this.sTRY = sTRY;
    }
    
    public Bank(String name, double bUSD, double sUSD, double bEUR, double sEUR, double bRUB, double sRUB, double bGBP, double sGBP, double bTRY, double sTRY, LocalDate date) {
        this.name = name;
        this.bUSD = bUSD;
        this.sUSD = sUSD;
        this.bEUR = bEUR;
        this.sEUR = sEUR;
        this.bRUB = bRUB;
        this.sRUB = sRUB;
        this.bGBP = bGBP;
        this.sGBP = sGBP;
        this.bTRY = bTRY;
        this.sTRY = sTRY;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getbUSD() {
        return bUSD;
    }

    public void setbUSD(double bUSD) {
        this.bUSD = bUSD;
    }

    public double getsUSD() {
        return sUSD;
    }

    public void setsUSD(double sUSD) {
        this.sUSD = sUSD;
    }

    public double getbEUR() {
        return bEUR;
    }

    public void setbEUR(double bEUR) {
        this.bEUR = bEUR;
    }

    public double getsEUR() {
        return sEUR;
    }

    public void setsEUR(double sEUR) {
        this.sEUR = sEUR;
    }

    public double getbRUB() {
        return bRUB;
    }

    public void setbRUB(double bRUB) {
        this.bRUB = bRUB;
    }

    public double getsRUB() {
        return sRUB;
    }

    public void setsRUB(double sRUB) {
        this.sRUB = sRUB;
    }

    public double getbTRY() {
        return bTRY;
    }

    public void setbTRY(double bTRY) {
        this.bTRY = bTRY;
    }

    public double getsTRY() {
        return sTRY;
    }

    public void setsTRY(double sTRY) {
        this.sTRY = sTRY;
    }

    public double getbGBP() {
        return bGBP;
    }

    public void setbGBP(double bGBP) {
        this.bGBP = bGBP;
    }

    public double getsGBP() {
        return sGBP;
    }

    public void setsGBP(double sGBP) {
        this.sGBP = sGBP;
    }
    
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "\nBank{" + "name=" + name + ", bUSD=" + bUSD + ", sUSD=" + sUSD + ", bEUR=" + bEUR + ", sEUR=" + sEUR + ", bRUB=" + bRUB + ", sRUB=" + sRUB + ", bGBP=" + bGBP + ", sGBP=" + sGBP + ", bTRY=" + bTRY + ", sTRY=" + sTRY +", date=" + date + '}';
    }





    
}
