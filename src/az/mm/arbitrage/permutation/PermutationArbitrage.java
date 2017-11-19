package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.*;
import az.mm.arbitrage.factory.*;
import java.util.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class PermutationArbitrage implements Arbitrage {

    private Map<Integer, List<List<String>>> allPermutationMap;
    private String baseCurrency, currencies[];
    private int count;
    
    
    @Override
    public void start(Data data) {
        currencies = data.getCurrencies();
        Scanner sc = new Scanner(System.in);
        System.out.println("Currency list: " + String.join(" ", currencies));
        System.out.println("Select base currency from the list: ");
        baseCurrency = sc.next().toUpperCase();
        
        System.out.println("Select currencies which you want:");
        sc.nextLine(); //bunu yazmayanda Exception verir..
        String curString = sc.nextLine();
        
        List<String> tempList = "".equals(curString) ? Arrays.asList(currencies) : Arrays.asList(curString.trim().toUpperCase().split(" "));
        tempList = new ArrayList(tempList); // prevent UnsupportedOperationException
        tempList.removeIf(s -> s.equals(baseCurrency));
        currencies = tempList.toArray(new String[tempList.size()]);
        
        allPermutationMap = new LinkedHashMap<>();
        Permutation p = new Permutation(getOptimalRatesMap(data), baseCurrency);
        List<String> curList = Arrays.asList(currencies);
        System.out.println("");

        for (int i = 1; i <= currencies.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
            allPermutationMap.put(i, p.permute(curList, i));
        }
        
        checkArbitrageOpportunity(p);
    }
    
    
    private Map<String, Map<String, OptimalRate>> getOptimalRatesMap(Data data) {
        String cur[] = Arrays.copyOf(currencies, currencies.length+1);
        cur[cur.length-1] = baseCurrency; 
//        OptimalRate [][] R = DataCache.getAdjencyMatrix(data, cur);   // Bunu niye gore ishletmeme sebebini DataCache classinda qeyd etmishem..
        OptimalRate [][] R = data.getOptimalRatesAdjencyMatrix(data, cur);
        Map<String, Map<String, OptimalRate>> ratesMap = new LinkedHashMap();
        Map<String, OptimalRate> curMap;

        for (int i = 0; i < R.length; i++) {
            curMap = new LinkedHashMap();
            for (int j = 0; j < R.length; j++) {
                if (i == j) continue;
                curMap.put(cur[j], R[i][j]);
            }
            ratesMap.put(cur[i], curMap);
        }

        return ratesMap;
    }


    private void checkArbitrageOpportunity(Permutation p){
        Map<Double, List<PermutationArbitrageModel>> arbitrageListMap = p.getArbitrageListMap();
        if(arbitrageListMap.isEmpty())
            System.out.println("No arbitrage opportunity!\n");
        else {       
//            arbitrageListMap = sortedMaxProfit(arbitrageListMap); 
            arbitrageListMap.forEach((key, value) -> {
                System.out.printf("\nArbitrage %d: (profit - %.2f %s) \n", ++count, key, baseCurrency);
                value.forEach(a -> {
                    System.out.printf("%10.4f %s = %10.4f %s (%s)\n", a.getFirstResult(), a.getFromCur(), a.getLastResult(), a.getToCur(), a.getBankName());
                });
            });
        }
    }
    
    private Map<Double, List<PermutationArbitrageModel>> sortedMaxProfit(Map<Double, List<PermutationArbitrageModel>> arbitrageListMap){
        Map<Double, List<PermutationArbitrageModel>> treeMap = new TreeMap<>(
                    (Comparator<Double>) (o1, o2) -> o2.compareTo(o1) ); //for desc order
        treeMap.putAll(arbitrageListMap);
        
        return treeMap;
    }
    
    
    private void printMap(Map<String, Map<String, OptimalRate>> map){
        map.forEach((k,v) -> {
            System.out.printf("\n%-5s",k);
            v.forEach((k2,opt) -> {
                System.out.printf("->%s: %.4f - %-17s ", k2, opt.getValue(), opt.getBankName());
            });
        });
    }

    
    private void printAllPermutations() {
        count = 0;
        allPermutationMap.forEach((k, v) -> {
            System.out.printf("%n%d - say: %d%n", k, v.size());
            v.forEach((v2) -> {
                System.out.printf("%d. %s --> ", ++count, baseCurrency);
                for (String s : v2) 
                    System.out.print(s + " --> ");
                System.out.println(baseCurrency);
            });
        });
    }
    
}
