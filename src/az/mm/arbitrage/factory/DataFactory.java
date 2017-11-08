package az.mm.arbitrage.factory;

import az.mm.arbitrage.data.AniMezenneData;
import az.mm.arbitrage.data.AznTodayData;
import az.mm.arbitrage.data.ExcelData;
import az.mm.arbitrage.data.JsonData;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */

/**
 * 
 * Factory pattern
 */
public class DataFactory extends AbstractFactory {
    
    @Override
    public Data getData(int n){
         switch (n) {
            case 1:  return new ExcelData(n);      
            case 2:  return new AznTodayData(n);   
            case 3:  return new AniMezenneData(n);   
            case 4:  return new JsonData(n);       
            default: return new ExcelData(n);      
        }
    }

 
    @Override
    public Arbitrage getArbitrage(int type) {
        return null;
    }
}
