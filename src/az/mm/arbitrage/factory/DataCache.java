package az.mm.arbitrage.factory;

import az.mm.arbitrage.model.OptimalRate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class DataCache {

    private static Map<Integer, OptimalRate [][]> cachedDataMap = new HashMap();

    public static OptimalRate [][] getAdjencyMatrix(Data data, String[] currencies) {
        OptimalRate [][] optArr;
        int key = data.getDataId();
        if(cachedDataMap.containsKey(key))
            optArr = cachedDataMap.get(key);
        else {
            optArr = data.getOptimalRatesAdjencyMatrix(data, currencies);
            cachedDataMap.put(key, optArr);
        }
        
        return optArr;
    }
}
