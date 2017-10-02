package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.OptimalRate;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author USER
 */
public class Arbitrage {
    private final Map<String, Map<String, OptimalRate>> ratesMap;
    private int arbitrageNumber;
    private final String baseCurrency;

    public Arbitrage(Map<String, Map<String, OptimalRate>> map, String baseCurrency) {
        ratesMap = map;
        this.baseCurrency = baseCurrency;
    }
 

    public boolean isArbitrage(ArrayList<String> list) {
        String from, to;
        double result = 1000;
        StringBuilder sb = new StringBuilder();
        OptimalRate opt;
        for (int i = 0; i < list.size(); i++) {
            from = (i == 0) ? baseCurrency : list.get(i - 1);
            to = list.get(i);
            opt = getRate(from, to);
            sb.append(round(result)).append(" "+from+" = ");
            result *= opt.getValue();
            sb.append(round(result)).append(" "+to).append(" ("+opt.getName()+")\n");
        }
        from = list.get(list.size()-1);
        to = baseCurrency;
        opt = getRate(from, to);
        sb.append(round(result)).append(" "+from+" = ");
        result *= opt.getValue();
        sb.append(round(result)).append(" "+to).append(" ("+opt.getName()+")\n");
        
        if(result>1000){
//            if(result-1000<20) return true;
            System.out.printf("Arbitrage %d:\n", ++arbitrageNumber);
            System.out.println(sb);
            return true;
        }

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
}
