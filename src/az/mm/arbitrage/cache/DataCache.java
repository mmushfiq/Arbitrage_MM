package az.mm.arbitrage.cache;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.OptimalRate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class DataCache {

    private static final Map<Integer, OptimalRate [][]> cachedDataMap = new HashMap();

     /**
     * Map.a cached edilmish array deyishmir, oldugu kimi qalir, burda problem 
     * yoxdur. Problem ondadir ki, permutation arbitrage alqoritmi sechildikde 
     * currency siralamasi (sayi da ola biler) deyishir, amma map.a
     * 1-ci hansi array atilibsa o qalir ve bu da duzgun netice vermir. Ona gore de
     * PermutationArbitrage alqoritmi sechildikde hele ki, bu metodu chagirmayacam, 
     * sechilmish currency-ye uygun her defe yeni array yaradib istifade edecem. 
     * Gelecekde bunun optimallashdirilmasina baxacam..
     */
    public static OptimalRate [][] getAdjencyMatrix(Data data, String[] currencies) {
        OptimalRate [][] optArr;
        int key = data.getDataId();
        if(cachedDataMap.containsKey(key))
            optArr = cachedDataMap.get(key).clone();
        else {
            optArr = data.getOptimalRatesAdjencyMatrix(data, currencies);
            cachedDataMap.put(key, optArr);
        }
        
        return optArr;
    }
    
}
