package az.mm.arbitrage.permutation;

/**
 *
 * @author MM
 */
public class ArbitrageModel {
    private double firstResult, lastResult;
    private String fromCur, toCur, bankName;

    public ArbitrageModel() {
    }

    public ArbitrageModel(double firstResult, double lastResult, String fromCur, String toCur, String bankName) {
        this.firstResult = firstResult;
        this.lastResult = lastResult;
        this.fromCur = fromCur;
        this.toCur = toCur;
        this.bankName = bankName;
    }

    public double getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(double firstResult) {
        this.firstResult = firstResult;
    }

    public double getLastResult() {
        return lastResult;
    }

    public void setLastResult(double lastResult) {
        this.lastResult = lastResult;
    }

    public String getFromCur() {
        return fromCur;
    }

    public void setFromCur(String fromCur) {
        this.fromCur = fromCur;
    }

    public String getToCur() {
        return toCur;
    }

    public void setToCur(String toCur) {
        this.toCur = toCur;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "ArbitrageModel{" + "firstResult=" + firstResult + ", lastResult=" + lastResult + ", fromCur=" + fromCur + ", toCur=" + toCur + ", bankName=" + bankName + '}';
    }
    
    
    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
