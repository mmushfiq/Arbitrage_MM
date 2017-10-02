package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.OptimalRate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MM
 */
public class Permutation {

    private Arbitrage arb;
    private int count;
    private final String baseCurrency;
    private List<ArrayList<String>> arbitragePermutations;

    public Permutation(Map<String, Map<String, OptimalRate>> map, String baseCurrency) {
        arb = new Arbitrage(map, baseCurrency);
        this.baseCurrency = baseCurrency;
        arbitragePermutations = new ArrayList<>();
    }

    // a is the original array
    // k is the number of elements in each permutation
    public ArrayList<ArrayList<String>> permute(List<String> a, int k) {
        ArrayList<ArrayList<String>> allPermutations = new ArrayList<ArrayList<String>>();
        enumerate(a, a.size(), k, allPermutations);
        return allPermutations;
    }

    // a is the original array
    // n is the array size
    // k is the number of elements in each permutation
    // allPermutations is all different permutations
    private void enumerate(List<String> a, int n, int k, ArrayList<ArrayList<String>> allPermutations) {
        if (k == 0) {
            ArrayList<String> singlePermutation = new ArrayList<>();
            for (int i = n; i < a.size(); i++) {
                singlePermutation.add(a.get(i));
            }
            if(arb.isArbitrage(singlePermutation)) 
                arbitragePermutations.add(singlePermutation);
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
    private void swap(List<String> a, int i, int j) {
        String temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    private void printPermutation(List<String> permutation) {
        System.out.println(" - say: " + permutation.size());
        System.out.printf("%d. %s --> ", count++, baseCurrency);
        for (String s : permutation) {
            System.out.print(s + " --> ");
        }
        System.out.println(baseCurrency);
    }

    public List<ArrayList<String>> getArbitragePermutations() {
        return new ArrayList<ArrayList<String>>(arbitragePermutations);
    }
    
    

}
