package az.mm.arbitrage.model;

/**
 *
 * @author MM
 */
public class OptimalRate {
    private int id;
    private String bankName;
    private Double value;

    public OptimalRate() {
        id = 0;
        bankName = "";
        value = Double.MIN_VALUE;
    }

    public OptimalRate(int id, String name, Double value) {
        this.id = id;
        this.bankName = name;
        this.value = value;
    }
    
    public OptimalRate(String name, Double value) {
        this.bankName = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\nOptimalRate{name=" + bankName + ", value=" + value + '}';
    }
    
    
}
