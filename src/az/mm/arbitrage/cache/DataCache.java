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

    public static OptimalRate [][] getAdjencyMatrix(Data data, String[] currencies) {
        OptimalRate [][] optArr;
        int key = data.getDataId();
        if(cachedDataMap.containsKey(key)){
            optArr = cachedDataMap.get(key);
            System.out.println("cache data: ");
            data.printArr(data, optArr, currencies);
        }
        else {
            optArr = data.getOptimalRatesAdjencyMatrix(data, currencies);
            cachedDataMap.put(key, optArr);
        }
        
        return optArr;
    }
}
