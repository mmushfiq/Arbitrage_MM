package az.mm.arbitrage.permutation;

import az.mm.arbitrage.model.*;
import java.util.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class Permutation {

    private final ArbitrageChecker arb;

    public Permutation(Map<String, Map<String, OptimalRate>> map, String baseCurrency) {
        arb = new ArbitrageChecker(map, baseCurrency);
    }

    
    // k is the number of elements in each permutation
    public List<List<String>> permute(List<String> a, int k) {
        List<List<String>> allPermutations = new ArrayList();
        enumerate(a, a.size(), k, allPermutations);
        return allPermutations;
    }


    private void enumerate(List<String> a, int n, int k, List<List<String>> allPermutations) {
        if (k == 0) {
            List<String> singlePermutation = new ArrayList();
            for (int i = n; i < a.size(); i++) {
                singlePermutation.add(a.get(i));
            }
            arb.hasArbitrage(singlePermutation); 
            allPermutations.add(singlePermutation);
            return;
        }

        for (int i = 0; i < n; i++) {
            swap(a, i, n - 1);
            enumerate(a, n - 1, k - 1, allPermutations);
            swap(a, i, n - 1);
        }
    }

    private void swap(List<String> a, int i, int j) {
        String temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }
    
    public Map<Double, List<PermutationArbitrageModel>> getArbitrageListMap(){
        return arb.getArbitrageListMap();
    }
    
}
