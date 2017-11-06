package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.PermutationArbitrageModel;
import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.factory.Arbitrage;
import az.mm.arbitrage.model.OptimalRate;
import java.util.*;

/**
 *
 * @author MM
 */
public class PermutationArbitrage implements Arbitrage {

    private Map<Integer, ArrayList<ArrayList<String>>> allPermutationMap;
    private static String baseCurrency;
    private String[] currencies;
    private int count;
    
    
    @Override
    public void start(Data data) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Currency list: AZN USD EUR GBP RUB TRY");
        System.out.println("Select base currency from the list: ");
        baseCurrency = sc.next().toUpperCase();
        
        System.out.println("Select currencies which you want:");
        sc.nextLine(); //bunu yazmayanda Exception verir..
        String curString = sc.nextLine();
        currencies = curString.trim().toUpperCase().split(" ");
        
        allPermutationMap = new LinkedHashMap<>();

        Permutation p = new Permutation(getOptimalRatesMap(data), baseCurrency);
        List<String> curList = Arrays.asList(currencies);
        System.out.println("");

        for (int i = 1; i <= currencies.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
            allPermutationMap.put(i, p.permute(curList, i));
        }
        
        checkArbitrageOpportunity(p);
//        printAllPermutations();
    }
    
    
    private Map<String, Map<String, OptimalRate>> getOptimalRatesMap(Data data) {
        String cur[] = Arrays.copyOf(currencies, currencies.length+1);
        cur[cur.length-1] = baseCurrency; 
        OptimalRate [][] R = data.getOptimalRatesAdjencyMatrix(data.getBankList(), cur);
        Map<String, Map<String, OptimalRate>> ratesMap = new HashMap();
        Map<String, OptimalRate> curMap;

        for (int i = 0; i < R.length; i++) {
            curMap = new HashMap();
            for (int j = 0; j < R.length; j++) {
                if (i == j) continue;
                curMap.put(cur[j], R[i][j]);
            }
            ratesMap.put(cur[i], curMap);
        }
        
        printMap(ratesMap);

        return ratesMap;
    }


    private void checkArbitrageOpportunity(Permutation p){
//        Arbitrage arb = new Arbitrage(); 
//        Map<Integer, List<ArbitrageModel>> arbitrageListMap = arb.getArbitrageListMap();  //arb obyekti ile birbasha bunu almaq olmur, Permutation obyekti ile elaqelendirib almaq lazimdi, ona gore de helelik map.i static eledim..
        
        Map<Double, List<PermutationArbitrageModel>> arbitrageListMap = p.getArbitrageListMap();
        
        if(arbitrageListMap.isEmpty())
            System.out.println("No arbitrage opportunity!\n");
        else {
            Map<Double, List<PermutationArbitrageModel>> treeMap = new TreeMap<>(
                    (Comparator<Double>) (o1, o2) -> o2.compareTo(o1) //for desc order
            );
            treeMap.putAll(arbitrageListMap);
            treeMap.forEach((key, value) -> {
                System.out.printf("\nArbitrage %d: (profit - %.2f %s) \n", ++count, key, baseCurrency);
                for(PermutationArbitrageModel a: value)
                    System.out.printf("%.4f %s = %.4f %s (%s)\n", a.getFirstResult(), a.getFromCur(), a.getLastResult(), a.getToCur(), a.getBankName());
            });
        }
    }
    
    
    private void printMap(Map<String, Map<String, OptimalRate>> map){
        map.forEach((k,v) -> {
            System.out.printf("\n%-5s",k);
            v.forEach((k2,v2) -> {
                System.out.printf("->%s: %.4f - %-17s ", k2, v2.getValue(), v2.getBankName());
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
