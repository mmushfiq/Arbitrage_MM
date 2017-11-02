package az.mm.arbitrage.permutation;

import az.mm.arbitrage.data.AniMezenneData;
import az.mm.arbitrage.data.AznTodayData;
import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.data.ExcelData;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MM
 */
public class ArbitragePermutationCopy {

    static int countK;
    static int arbitrageNumber;
    private static Map<String, Map<String, OptimalRate>> ratesMap;

    static {
        ratesMap = getOptimalRatesMap();
    }

    // a is the original array
    // k is the number of elements in each permutation
    public static ArrayList<ArrayList<String>> choose(ArrayList<String> a, int k) {
        ArrayList<ArrayList<String>> allPermutations = new ArrayList<ArrayList<String>>();
        enumerate(a, a.size(), k, allPermutations);
        return allPermutations;
    }

    // a is the original array
    // n is the array size
    // k is the number of elements in each permutation
    // allPermutations is all different permutations
    private static void enumerate(ArrayList<String> a, int n, int k, ArrayList<ArrayList<String>> allPermutations) {
        if (k == 0) {
            countK++;
            ArrayList<String> singlePermutation = new ArrayList<>();
            for (int i = n; i < a.size(); i++) {
                singlePermutation.add(a.get(i));
            }
//            isArbitrage(singlePermutation);
            allPermutations.add(singlePermutation);
            return;
        }

        for (int i = 0; i < n; i++) {
            swap(a, i, n - 1);
            enumerate(a, n - 1, k - 1, allPermutations);
            swap(a, i, n - 1);
        }
    }

    // helper function that swaps a.get(i) and a.get(j)
    public static void swap(ArrayList<String> a, int i, int j) {
        String temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // sample client
    public static void main(String[] args) {

        // n is the end item of the array.
        // if n = 5, the array is [0, 1, 2, 3, 4, 5]
        // k is the number of elements of each permutation.
        int n = 8;
        int k = 5;

        String[] arr = {/*"AZN",*/"USD", "EUR", "GBP", "RUB", "TRY","CAD", "AUD", "CHF"};

        // create original array
        ArrayList<String> elements = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            elements.add(arr[i]);
        }

        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            a.add(elements.get(i));
        }
//        ArrayList<ArrayList<String>> permutationList = choose(a, k);

        Map<Integer, ArrayList<ArrayList<String>>> allPermutationMap = new LinkedHashMap<Integer, ArrayList<ArrayList<String>>>();
        for (int i = 1; i <= arr.length; i++) {  //burada i necheli permutasiya lazimdisa onu bildirir, yuxaridaki k-ni evz edir. Bize butun permutasiyalar lazim oldugundan tek tek gonderirik..
            allPermutationMap.put(i, choose(a, i));
        }

        int count = 1;
        System.out.println("countK = " + countK);
        for (Map.Entry<Integer, ArrayList<ArrayList<String>>> entry : allPermutationMap.entrySet()) {
            System.out.print(entry.getKey());
            List<ArrayList<String>> value = entry.getValue();
            System.out.println(" - say: " + value.size());
            for (ArrayList<String> list : value) {
                System.out.print(count++ + ". AZN --> ");
                for (String s : list) {
                    System.out.print(s + " --> ");
                }
                System.out.println("AZN");
            }

            System.out.println("\n");
        }
    }

    private static Map<String, Map<String, OptimalRate>> getOptimalRatesMap() {
        Data data = new ExcelData();
//        Data data = new AznTodayData();
//        Data data = new AniMezenneData();
//        Map<String, Map<String, OptimalRate>> ratesMap = data.getOptimalRatesMap(data.getBankList()); //bu metod achilmalidi..
        Map<String, Map<String, OptimalRate>> ratesMap = new HashMap();

        return ratesMap;
    }

    private static boolean isArbitrage(ArrayList<String> list) {
        String from, to;
        double result = 1000;
        StringBuilder sb = new StringBuilder();
        OptimalRate opt;
        for (int i = 0; i < list.size(); i++) {
            from = (i == 0) ? "AZN" : list.get(i - 1);
            to = list.get(i);
            opt = getRate(from, to);
            sb.append(result).append(" "+from+" = ");
            result *= opt.getValue();
            sb.append(result).append(" "+to).append(" ("+opt.getName()+")\n");
        }
        from = list.get(list.size()-1);
        to = "AZN";
        opt = getRate(from, to);
        sb.append(result).append(" "+from+" = ");
        result *= opt.getValue();
        sb.append(result).append(" "+to).append(" ("+opt.getName()+")\n");
        
        if(result>1000){
            System.out.printf("Arbitrage %d:\n", ++arbitrageNumber);
            System.out.println(sb);
            return true;
        }

        return false;
    }

    private static OptimalRate getRate(String from, String to) {
        Map<String, OptimalRate> currency = ratesMap.get(from);
        OptimalRate optimalRate = currency.get(to);

        return optimalRate;
    }

}