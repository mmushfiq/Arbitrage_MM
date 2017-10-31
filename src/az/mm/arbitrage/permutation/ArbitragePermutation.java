package az.mm.arbitrage.permutation;

import az.mm.arbitrage.data.*;
import az.mm.arbitrage.factory.Algorithm;
import az.mm.arbitrage.model.*;
import java.util.*;

/**
 *
 * @author MM
 */
public class ArbitragePermutation implements Algorithm {

    private Map<Integer, ArrayList<ArrayList<String>>> allPermutationMap;
    private static String baseCurrency;
    private int count;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Currency list: AZN USD EUR GBP RUB TRY");
        System.out.println("Select base currency from the list: ");
        baseCurrency = sc.next().toUpperCase();
//        baseCurrency = "AZN";
        System.out.println("Select currencies which you want:");
        sc.nextLine(); //bunu yazmayanda Exception verir..
        String curString = sc.nextLine();
        String[] currencies = curString.trim().toUpperCase().split(" ");

//        String[] currencies = {/*"AZN",*/"USD", "EUR", "GBP", "RUB", "TRY",};
        ArbitragePermutation ap = new ArbitragePermutation();
        ap.start(currencies);
//        ap.startForAniMezenne(currencies);

    }

    private void start(String[] currencies) {
        allPermutationMap = new LinkedHashMap<>();
        Permutation p = new Permutation(getOptimalRatesMap(currencies), baseCurrency);
        List<String> curList = Arrays.asList(currencies);
        System.out.println("");

        for (int i = 1; i <= currencies.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir, yuxaridaki k-ni evz edir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
            allPermutationMap.put(i, p.permute(curList, i));
        }
        
        checkArbitrageOpportunity(p);
        printAllPermutations();
    }

    private void startForAniMezenne(String[] currencies) {
        allPermutationMap = new LinkedHashMap<Integer, ArrayList<ArrayList<String>>>();
        AniMezenneData d = new AniMezenneData();

        List<Bank> partList = new ArrayList<>();
        Date date = null;

        for (Bank b : d.getBankList()) { //d meselesine bir de baxmaq..
            if (date == null) date = b.getDate();
            
            if (date.equals(b.getDate())) partList.add(b);
            else {
                System.out.println("\n---------------------------------\n" + date);
                Map<String, Map<String, OptimalRate>> ratesMap = d.getOptimalRatesMap(partList, baseCurrency, currencies);
                Permutation p = new Permutation(ratesMap, baseCurrency);
                List<String> curList = Arrays.asList(currencies);
                System.out.println("");

                for (int i = 1; i <= currencies.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir, yuxaridaki k-ni evz edir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
                    allPermutationMap.put(i, p.permute(curList, i));
                }

                checkArbitrageOpportunity(p);
                
                partList.clear();
                date = b.getDate();
            }
        }
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

    private static Map<String, Map<String, OptimalRate>> getOptimalRatesMap(String[] cur) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nSelect data: \n 1 - Excel, 2 - AznToday, 3 - AniMezenne, 4 - Json");
        int n = sc.nextInt();
        DataFactory factory = new DataFactory();
        Data d = factory.getData(n);
        Map<String, Map<String, OptimalRate>> ratesMap = d.getOptimalRatesMap(d.getBankList(), baseCurrency, cur);
//        Map<String, Map<String, OptimalRate>> ratesMap = d.getOptimalRatesMap(d.getBankList());
        
        return ratesMap;
    }
    
    public void checkArbitrageOpportunity(Permutation p){
//        Arbitrage arb = new Arbitrage(); 
//        Map<Integer, List<ArbitrageModel>> arbitrageListMap = arb.getArbitrageListMap();  //arb obyekti ile birbasha bunu almaq olmur, Permutation obyekti ile elaqelendirib almaq lazimdi, ona gore de helelik map.i static eledim..
        
        Map<Double, List<ArbitrageModel>> arbitrageListMap = p.getArbitrageListMap();
        
        if(arbitrageListMap.isEmpty())
            System.out.println("No arbitrage opportunity!\n");
        else {
            Map<Double, List<ArbitrageModel>> treeMap = new TreeMap<>(
                    (Comparator<Double>) (o1, o2) -> o2.compareTo(o1) //for desc order
            );
            treeMap.putAll(arbitrageListMap);
            treeMap.forEach((key, value) -> {
                System.out.printf("\nArbitrage %d: (profit - %.2f %s) \n", ++count, key, baseCurrency);
                for(ArbitrageModel a: value)
                    System.out.printf("%.4f %s = %.4f %s (%s)\n", a.getFirstResult(), a.getFromCur(), a.getLastResult(), a.getToCur(), a.getBankName());
            });
        }
    }

}
