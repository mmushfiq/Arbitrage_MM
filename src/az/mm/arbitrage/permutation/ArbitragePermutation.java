package az.mm.arbitrage.permutation;

import az.mm.arbitrage.data.AniMezenneData;
import az.mm.arbitrage.data.AznTodayData;
import az.mm.arbitrage.data.Data;
import az.mm.arbitrage.data.ExcelData;
import az.mm.arbitrage.data.JsonData;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author MM
 */
public class ArbitragePermutation {

    private Map<Integer, ArrayList<ArrayList<String>>> allPermutationMap;
    private static String baseCurrency;

    // sample client
    public static void main(String[] args) {

        // n is the end item of the array.
        // if n = 5, the array is [0, 1, 2, 3, 4, 5]
        // k is the number of elements of each permutation.
//        int n = 5;
//        int k = 5;
        Scanner sc = new Scanner(System.in);
        System.out.println("Currency list: AZN USD EUR GBP RUB TRY");
        System.out.println("Select base currency from the list: ");
        baseCurrency = sc.next();
        System.out.println("Select currencies which you want:");
        sc.nextLine(); //bunu yazmayanda Exception verir..
        String curString = sc.nextLine();
        String[] currencies = curString.split(" ");

//        String[] currencies = {/*"AZN",*/"USD", "EUR", "GBP", "RUB", "TRY",};
        ArbitragePermutation ap = new ArbitragePermutation();
        ap.start(currencies);
//        ap.startForAniMezenne(currencies);

    }

    private void start(String[] currencies) {
        allPermutationMap = new LinkedHashMap<Integer, ArrayList<ArrayList<String>>>();
        Permutation p = new Permutation(getOptimalRatesMap(), baseCurrency);
        List<String> curList = Arrays.asList(currencies);
        System.out.println("");

        for (int i = 1; i <= currencies.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir, yuxaridaki k-ni evz edir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
            allPermutationMap.put(i, p.permute(curList, i));
        }

        if (p.getArbitragePermutations().size() == 0) {
            System.out.println("No arbitrage opportunity!\n");
        }

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
                Map<String, Map<String, OptimalRate>> ratesMap = d.getOptimalRatesMap(partList);
                Permutation p = new Permutation(ratesMap, baseCurrency);
                List<String> curList = Arrays.asList(currencies);
                System.out.println("");

                for (int i = 1; i <= currencies.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir, yuxaridaki k-ni evz edir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
                    allPermutationMap.put(i, p.permute(curList, i));
                }

                if (p.getArbitragePermutations().size() == 0) {
                    System.out.println("No arbitrage opportunity!\n");
                }

                partList.clear();
                date = b.getDate();
            }
        }
    }

    private void printAllPermutations() {
        int count = 1;
        for (Map.Entry<Integer, ArrayList<ArrayList<String>>> entry : allPermutationMap.entrySet()) {
            System.out.print(entry.getKey());
            List<ArrayList<String>> value = entry.getValue();
            System.out.println(" - say: " + value.size());
            for (ArrayList<String> list : value) {
                System.out.printf("%d. %s --> ", count++, baseCurrency);
                for (String s : list) {
                    System.out.print(s + " --> ");
                }
                System.out.println(baseCurrency);
            }

            System.out.println("\n");
        }
    }

    private static Map<String, Map<String, OptimalRate>> getOptimalRatesMap() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nSelect data: \n 1 - Excel, 2 - AznToday, 3 - AniMezenne, 4 - Json");
        int n = sc.nextInt();
        Data d;
        switch (n) {
            case 1:
                d = new ExcelData();
                break;
            case 2:
                d = new AznTodayData();
                break;
            case 3:
//                d = new AniMezenneData();  //butun melumatlari eyni vaxtda verende duzgun ishlemir, gun gun yoxlamaq lazimdi..
//                break;
            case 4:
                d = new JsonData();
                break;
            default:
                d = new ExcelData();
                break;
        }

        Map<String, Map<String, OptimalRate>> ratesMap = d.getOptimalRatesMap(d.getBankList());
        
        return ratesMap;
    }

}
