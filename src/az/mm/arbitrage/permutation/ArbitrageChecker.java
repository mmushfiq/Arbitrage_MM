package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.*;
import java.util.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class ArbitrageChecker {
    private final Map<String, Map<String, OptimalRate>> ratesMap;
    private String baseCurrency;
    private Map<Double, List<PermutationArbitrageModel>> arbitrageListMap;

    public ArbitrageChecker() {
        ratesMap = new HashMap();
    }
    
    public ArbitrageChecker(Map<String, Map<String, OptimalRate>> map, String baseCurrency) {
        ratesMap = map;
        this.baseCurrency = baseCurrency;
        arbitrageListMap = new LinkedHashMap<>();
    }
 

    public boolean hasArbitrage(List<String> list) {
        String from, to;
        double startValue = 1000;
        double result = startValue;
        OptimalRate opt;
        List<PermutationArbitrageModel> arbList = new ArrayList();
        
        for (int i = 0, size = list.size(); i <= size; i++) {
            if(i == size){
                from = list.get(size-1);
                to = baseCurrency;
            } else {
                from = (i == 0) ? baseCurrency : list.get(i - 1);
                to = list.get(i);
            }
            opt = getRate(from, to);
            if(opt.getValue() == -1) return false;  // no conversion
            arbList.add(new PermutationArbitrageModel(result, result *= opt.getValue(), from, to, opt.getBankName()));
        }
        
        if(result > startValue){
            arbitrageListMap.put(result-startValue, arbList); //eger profit eyni olsa insert yox update olacaq ve itki bash verecek.. 
            return true;
        } 
        else
            arbList = null; //for helping GC

        return false;
    }

    private OptimalRate getRate(String from, String to) {
        Map<String, OptimalRate> currency = ratesMap.get(from);
        OptimalRate optimalRate = currency.get(to);

        return optimalRate;
    }
    
    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
    
    public Map<Double, List<PermutationArbitrageModel>> getArbitrageListMap(){
        return new LinkedHashMap<>(arbitrageListMap);
    }
}
