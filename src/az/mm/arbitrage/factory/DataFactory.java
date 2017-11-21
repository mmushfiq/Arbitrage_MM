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
            default: throw new IllegalArgumentException("Incorrect number! Please enter the number between 1 and 4.");
        }
    }

 
    @Override
    public Arbitrage getArbitrage(int type) {
        return null;
    }
}
