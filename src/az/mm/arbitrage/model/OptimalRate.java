package az.mm.arbitrage.model;

/**
 *
 * @author MM
 */
public class OptimalRate {
    private int id;
    private String name;
    private Double value;

    public OptimalRate() {
        id = 0;
        name = "";
        value = Double.MIN_VALUE;
    }

    public OptimalRate(int id, String name, Double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
    
    public OptimalRate(String name, Double value) {
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OptimalRate{" + "id=" + id + ", name=" + name + ", value=" + value + '}';
    }
    
    
}
