package az.mm.arbitrage.factory;

import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MM
 */
public abstract class Data {

    public abstract List<Bank> getBankList();
    
    
    public OptimalRate [][] getOptimalRatesAdjencyMatrix(List<Bank> bankList, String[] cur) {
        
        double curRate, r1 = 0, r2 = 0;
        OptimalRate R[][] = new OptimalRate[cur.length][cur.length];

        for (Bank b : bankList)
            for (int i = 0; i < R.length; i++) 
                for (int j = 0; j < R.length; j++) {
                    
                    if (i == j){
                        R[i][j] = new OptimalRate("", 1.);
                        continue;
                    } 
                
                    switch (cur[i]) {
                        case "AZN": r1 = 1; break;
                        case "USD": r1 = b.getbUSD(); break;
                        case "EUR": r1 = b.getbEUR(); break;
                        case "RUB": r1 = b.getbRUB(); break;
                        case "GBP": r1 = b.getbGBP(); break;
                        case "TRY": r1 = b.getbTRY(); break;
                    }
                    
                    switch (cur[j]) {
                        case "AZN": r2 = 1; break;
                        case "USD": r2 = b.getsUSD(); break;
                        case "EUR": r2 = b.getsEUR(); break;
                        case "RUB": r2 = b.getsRUB(); break;
                        case "GBP": r2 = b.getsGBP(); break;
                        case "TRY": r2 = b.getsTRY(); break;
                    }

                    curRate = R[i][j] != null ? R[i][j].getValue() : Double.MIN_VALUE;
                    if (r1 > 0 && r2 > 0 && curRate < r1/r2){
                        R[i][j] = new OptimalRate(b.getName(), r1/r2);
                    }
                }

        printArr(R, cur);
        return R;
    }
    
    void printArr(OptimalRate[][] R, String[] cur){
        System.out.println("-----------------Adjency Matrix------------------");
        int i = 0;
        for(OptimalRate[] d: R){
            System.out.print(cur[i++]+"\t");
            for(OptimalRate m: d)
                System.out.printf("%.4f (%s)\t", m.getValue(), m.getBankName());
        System.out.println("");
        }
        
        System.out.println("-----------------Adjency Matrix------------------\n");
    }

    
    
//    /************************* OLD VERSION **********************************************
      
    public Map<String, Map<String, OptimalRate>> getOptimalRatesMap(List<Bank> bankList) {
        String[] currencies = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};
        Map<String, Map<String, OptimalRate>> ratesMap = new HashMap();
        try {
            for (int i = 0; i < currencies.length; i++) {
                Map<String, OptimalRate> map = new HashMap();
                for (int j = 0; j < currencies.length; j++) {
                    if (i == j) continue;
                    OptimalRate opt = getOptimalRates(currencies[i], currencies[j], bankList);
                    map.put(currencies[j], opt);
                }
                ratesMap.put(currencies[i], map);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            return ratesMap;
        }
    }

    // It is written elaborately to understand the above first getOptimalRatesMap() method.
    public OptimalRate getOptimalRates(String from, String to, List<Bank> bankList) {
        int id = 0;
        String name = null;
        double rate = Double.MAX_VALUE;
        OptimalRate opt = new OptimalRate(id, name, rate);

        switch (from + "-" + to) {
            //------------------------------------------------------------------
            case "AZN-USD":
                for (Bank b : bankList) {
                    if (rate > b.getsUSD()) {
                        rate = b.getsUSD();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, round(1 / rate));  //	0.5882
                return opt;

            case "AZN-EUR":
                for (Bank b : bankList) {
                    if (rate > b.getsEUR()) {
                        rate = b.getsEUR();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, round(1 / rate));
                return opt;

            case "AZN-GBP":
                for (Bank b : bankList) {
                    if (b.getsGBP() < 0) {
                        continue;
                    }
                    if (rate > b.getsGBP()) {
                        rate = b.getsGBP();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(1 / rate));
                return opt;

            case "AZN-RUB":
                for (Bank b : bankList) {
                    if (b.getsRUB() < 0) {
                        continue;
                    }
                    if (rate > b.getsRUB()) {
                        rate = b.getsRUB();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, round(1 / rate));
                return opt;

            case "AZN-TRY":
                for (Bank b : bankList) {
                    if (b.getsTRY() < 0) {
                        continue;
                    }
                    if (rate > b.getsTRY()) {
                        rate = b.getsTRY();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, round(1 / rate));
                return opt;

            //------------------------------------------------------------------    
            case "USD-AZN":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (rate < b.getbUSD()) {
                        rate = b.getbUSD();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, rate);
                return opt;

            case "USD-EUR":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (rate < b.getbUSD() / b.getsEUR()) {
                        rate = b.getbUSD() / b.getsEUR();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "USD-GBP":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbUSD() / b.getsGBP()) {
                        rate = b.getbUSD() / b.getsGBP();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "USD-RUB":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsRUB() < 0) {
                        continue;
                    }
                    if (rate < b.getbUSD() / b.getsRUB()) {
                        rate = b.getbUSD() / b.getsRUB();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "USD-TRY":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsTRY() < 0) {
                        continue;
                    }
                    if (rate < b.getbUSD() / b.getsTRY()) {
                        rate = b.getbUSD() / b.getsTRY();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            //------------------------------------------------------------------    
            case "EUR-AZN":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (rate < b.getbEUR()) {
                        rate = b.getbEUR();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, rate);
                return opt;

            case "EUR-USD":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (rate < b.getbEUR() / b.getsUSD()) {
                        rate = b.getbEUR() / b.getsUSD();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "EUR-GBP":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbEUR() / b.getsGBP()) {
                        rate = b.getbEUR() / b.getsGBP();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "EUR-RUB":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsRUB() < 0) {
                        continue;
                    }
                    if (rate < b.getbEUR() / b.getsRUB()) {
                        rate = b.getbEUR() / b.getsRUB();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "EUR-TRY":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsTRY() < 0) {
                        continue;
                    }
                    if (rate < b.getbEUR() / b.getsTRY()) {
                        rate = b.getbEUR() / b.getsTRY();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            //------------------------------------------------------------------    
            case "GBP-AZN":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbGBP()) {
                        rate = b.getbGBP();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, rate);
                return opt;

            case "GBP-USD":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbGBP() / b.getsUSD()) {
                        rate = b.getbGBP() / b.getsUSD();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "GBP-EUR":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbGBP() / b.getsEUR()) {
                        rate = b.getbGBP() / b.getsEUR();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "GBP-RUB":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbGBP() / b.getsRUB()) {
                        rate = b.getbGBP() / b.getsRUB();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "GBP-TRY":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsTRY() < 0 || b.getbGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbGBP() / b.getsTRY()) {
                        rate = b.getbGBP() / b.getsTRY();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            //------------------------------------------------------------------    
            case "RUB-AZN":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getsRUB() < 0) {
                        continue;
                    }
                    if (rate < b.getbRUB()) {
                        rate = b.getbRUB();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, rate);
                return opt;

            case "RUB-USD":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbRUB() < 0) {
                        continue;
                    }
                    if (rate < b.getbRUB() / b.getsUSD()) {
                        rate = b.getbRUB() / b.getsUSD();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "RUB-EUR":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbRUB() < 0) {
                        continue;
                    }
                    if (rate < b.getbRUB() / b.getsEUR()) {
                        rate = b.getbRUB() / b.getsEUR();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "RUB-GBP":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbRUB() < 0 || b.getsGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbRUB() / b.getsGBP()) {
                        rate = b.getbRUB() / b.getsGBP();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "RUB-TRY":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbRUB() < 0 || b.getsTRY() < 0) {
                        continue;
                    }
                    if (rate < b.getbRUB() / b.getsTRY()) {
                        rate = b.getbRUB() / b.getsTRY();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            //------------------------------------------------------------------    
            case "TRY-AZN":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbTRY() < 0) {
                        continue;
                    }
                    if (rate < b.getbTRY()) {
                        rate = b.getbTRY();
                        id = b.getId();
                        name = b.getName();
                    }
                }

                opt = new OptimalRate(id, name, rate);
                return opt;

            case "TRY-USD":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbTRY() < 0) {
                        continue;
                    }
                    if (rate < b.getbTRY() / b.getsUSD()) {
                        rate = b.getbTRY() / b.getsUSD();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "TRY-EUR":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbTRY() < 0) {
                        continue;
                    }
                    if (rate < b.getbTRY() / b.getsEUR()) {
                        rate = b.getbTRY() / b.getsEUR();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "TRY-GBP":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbTRY() < 0 || b.getsGBP() < 0) {
                        continue;
                    }
                    if (rate < b.getbTRY() / b.getsGBP()) {
                        rate = b.getbTRY() / b.getsGBP();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

            case "TRY-RUB":
                rate = Double.MIN_VALUE;
                for (Bank b : bankList) {
                    if (b.getbTRY() < 0 || b.getsRUB() < 0) {
                        continue;
                    }

                    if (rate < b.getbTRY() / b.getsRUB()) {
                        rate = b.getbTRY() / b.getsRUB();
                        id = b.getId();
                        name = b.getName();
                    }
                }
                opt = new OptimalRate(id, name, round(rate));
                return opt;

        }

        return opt;
    }
    
     private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
     }
    
//    * *************************************************************************/
  
}
