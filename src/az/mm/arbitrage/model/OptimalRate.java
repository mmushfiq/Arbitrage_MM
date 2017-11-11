package az.mm.arbitrage.model;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class OptimalRate {
//    private int id;
    private final String bankName;
    private final double value;

//    public OptimalRate() {
//        id = 0;
//        bankName = "";
//        value = Double.MIN_VALUE;
//    }
//
//    public OptimalRate(int id, String name, double value) {
//        this.id = id;
//        this.bankName = name;
//        this.value = value;
//    }
    
    public OptimalRate(String name, double value) {
        this.bankName = name;
        this.value = value;
    }

//    public int getId() {
//        return id;
//    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getBankName() {
        return bankName;
    }

//    public void setBankName(String bankName) {
//        this.bankName = bankName;
//    }

    public double getValue() {
        return value;
    }

//    public void setValue(double value) {
//        this.value = value;
//    }

    @Override
    public String toString() {
        return "\nOptimalRate{name=" + bankName + ", value=" + value + '}';
    }
    
    
}
