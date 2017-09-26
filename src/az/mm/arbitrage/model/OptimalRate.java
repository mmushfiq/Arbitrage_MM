package az.mm.arbitrage.model;

/**
 *
 * @author MM
 */
public class OptimalRate {
    private int id;
    private String name;
    private double value;

    public OptimalRate() {
        id = 0;
        name = "";
        value = Double.MAX_VALUE;
    }

    public OptimalRate(int id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OptimalRate{" + "id=" + id + ", name=" + name + ", value=" + value + '}';
    }
    
    
}
