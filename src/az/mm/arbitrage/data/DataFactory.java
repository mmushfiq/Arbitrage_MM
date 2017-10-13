package az.mm.arbitrage.data;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */

/**
 * 
 * Factory pattern
 */
public class DataFactory {
    public Data getData(int n){
         switch (n) {
            case 1:  return new ExcelData();      
            case 2:  return new AznTodayData();   
            case 3:  return new AniMezenneData();   //butun melumatlari eyni vaxtda verende duzgun ishlemir, gun gun yoxlamaq lazimdi.. 1-den chox gunu yoxlamaq uchun ayrica metod yazmisham yuxarida..
            case 4:  return new JsonData();       
            default: return new ExcelData();      
        }
    }
}
