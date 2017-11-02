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
            case 1:  return new ExcelData();      
            case 2:  return new AznTodayData();   
            case 3:  return new AniMezenneData();   //butun melumatlari eyni vaxtda verende duzgun ishlemir, gun gun yoxlamaq lazimdi.. 1-den chox gunu yoxlamaq uchun ayrica metod yazmisham yuxarida..
            case 4:  return new JsonData();       
            default: return new ExcelData();      
        }
    }

 
    @Override
    public Arbitrage getArbitrage(int type) {
        return null;
    }
}
