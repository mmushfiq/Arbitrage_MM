package az.mm.arbitrage.factory;

import az.mm.arbitrage.model.OptimalRate;
import java.util.Hashtable;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class DataCache {

    private static Hashtable<String, OptimalRate [][]> dataMap = new Hashtable();

//    public static Data getData(String dataId) {
//        Data cachedShape = dataMap.get(dataId);
//        return (Data) cachedShape.clone();
//    }
    
    public static OptimalRate [][] getAdjencyMatrix(Data data, String dataId) {
        OptimalRate [][] cachedMatrix = dataMap.get(dataId);
        return cachedMatrix.clone();
    }
}
