package az.mm.arbitrage.model;

/**
 * Bu classi immutable etdim ki, sonradan deyerlerini set edib deyishmek mumkun 
 * olmasin. Chunki yaradilmish obyekt cache edilib map.de saxlanilir, hansisa bir
 * alqoritm uchun deyerleri deyishdikde, novbeti defe cache edilmish array-i 
 * istifade etdikde netice sehv verir
 * 
 * @author MM <mushfiqazeri@gmail.com>
 */
public class OptimalRate {
    private final String bankName;
    private final double value;

    public OptimalRate(String bankName, double value) {
        this.bankName = bankName;
        this.value = value;
    }

    public String getBankName() {
        return bankName;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\nOptimalRate{bankName=" + bankName + ", value=" + value + '}';
    }
    
}
