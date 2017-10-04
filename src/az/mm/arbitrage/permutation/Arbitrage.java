package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.OptimalRate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class Arbitrage {
    private final Map<String, Map<String, OptimalRate>> ratesMap;
    private String baseCurrency;
    private Map<Double, List<ArbitrageModel>> arbitrageListMap;

    public Arbitrage() {
        ratesMap = null;
    }
    
    public Arbitrage(Map<String, Map<String, OptimalRate>> map, String baseCurrency) {
        ratesMap = map;
        this.baseCurrency = baseCurrency;
        arbitrageListMap = new LinkedHashMap<>();
    }
 

    public boolean isArbitrage(ArrayList<String> list) {
        String from, to;
        double startValue = 1000;
        double result = startValue;
//        StringBuilder sb = new StringBuilder(); //duzgun ishlese bunlari yigishdiracam..
        OptimalRate opt;
        List<ArbitrageModel> arbList = new ArrayList<>();
        
        for (int i = 0, size = list.size(); i <= size; i++) {
            if(i == size){
                from = list.get(size-1);
                to = baseCurrency;
            } else {
                from = (i == 0) ? baseCurrency : list.get(i - 1);
                to = list.get(i);
            }
            opt = getRate(from, to);
            result *= opt.getValue();
            arbList.add(new ArbitrageModel(round(result/opt.getValue()), round(result), from, to, opt.getName()));
        }
        
        if(result > startValue){
            arbitrageListMap.put(result-startValue, arbList);
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
    
    
    public Map<Double, List<ArbitrageModel>> getArbitrageListMap(){
        return new LinkedHashMap<>(arbitrageListMap);
    }
}
