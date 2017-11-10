package az.mm.arbitrage.bellmanford.mine;

import az.mm.arbitrage.factory.*;
import az.mm.arbitrage.model.OptimalRate;
import az.mm.arbitrage.princeton.Stack;
import java.util.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class BellmanFordArbitrage implements Arbitrage {
    private double[] dist;         // distance array
    private int[] p;               // predecessor array
    private OptimalRate adj[][];   // adjency matrix
    private final int INF = Integer.MAX_VALUE;
    private int vertex, source, number;
    private List<Integer> cycle;
    private Set<List> cycleList;
    private String currencies[];
    

    public BellmanFordArbitrage() {
        cycleList = new LinkedHashSet();
    }
    

    @Override
    public void start(Data data) {
//        adj = data.getOptimalRatesAdjencyMatrix(data.getBankList(), currencies);
        this.currencies = data.getCurrencies();
        adj = DataCache.getAdjencyMatrix(data, currencies);
        vertex = adj.length;
        source = 0;
        callBellmanFord();
    }
    

    public void callBellmanFord() {
        initializeSingleSource();
        for (int k = 1; k < vertex; k++)  //iterate (vertex-1)
            for (int u = 0; u < vertex; u++) 
                for (int v = 0; v < vertex; v++)  
                    relax(u, v);
            
        if (hasNegativeCycle()) checkArbitrage();
        else System.out.println("No arbitrage opportunity");
    }
    
    public void initializeSingleSource() {
        dist = new double[vertex];
        p = new int[vertex];
        for (int i = 0; i < vertex; i++) {
            dist[i] = INF;
            p[i] = -1;
        }
        dist[source] = 0;
    }
    

    public void relax(int u, int v) {
        if (u == v) return;
//        double weight = adj[u][v].getValue();
        double weight = -Math.log(adj[u][v].getValue());
        if (weight != INF /*&& dist[u] != INF*/ && (dist[v] > dist[u] + weight)) {   //if(d[u] != INFINITY && d[v] > d[u] + w) - lazim olsa bu sherti arashdirmaq mene lazimdi ya yox.
            dist[v] = dist[u] + weight;
            p[v] = u;
        }
    }
    
    private boolean hasNegativeCycle() {
        for (int u = 0; u < vertex; u++) 
            for (int v = 0; v < vertex; v++) {
                if(u == v) continue;
                double weight = adj[u][v].getValue();
//                double weight = -Math.log(adj[u][v].getValue());
                if (dist[v] > dist[u] + weight) 
                    findNegativeCyclePath(v);
            }
        
        return !cycleList.isEmpty();
    }
    
    private void findNegativeCyclePath(int v) {
        if(v == 0) return;
        boolean visited[] = new boolean[p.length];

        Stack<Integer> path = new Stack<>();
        for (; ; v = p[v]) {
            path.push(v);
            if (v == 0 || visited[v]) break;
            else visited[v] = true;
        }
        
        //hele ki duzgun ishlemedi..
//        do {
//            System.out.println("do while v: "+v);
//            path.push(v);
//            visited[v] = true;
//            v = p[v];
//        } while (v != 0 || !visited[v]);
        

        cycle = new ArrayList();
        for (Integer i : path) {
            if(cycle.contains(i)) break;
            cycle.add(i);
        }
        
        if (cycle.size() > 1) cycle.add(cycle.get(0));
        cycleList.add(cycle);
    }
    
    
    private void checkArbitrage(){
        /**
         * chox guman hesablamada hardasa xirda sehv var, ola bilsin ona gore chox cuzi ferqle
         * duzgun olmayan neqativ cycle. da detect olunur, onu arashdirib tapsam tamamdir
         * 
         * princeton numunesi:
            1000.00000 AZN =  453.40000 GBP (DəmirBank)
             453.40000 GBP = 1002.01400 AZN (Gunay Bank)
           menim numunem:
            1000.00000 AZN =  453.41192 GBP (DəmirBank)
             453.41192 GBP = 1002.04035 AZN (Gunay Bank)
         * 
         * */
        
        cycleList.forEach(v -> {
            System.out.printf("\nArbitrage %d: \n", ++number);
            double stake = 1000;
            for (int i = 0; i < v.size() - 1; i++) {
                int m = (int) v.get(i);
                int n = (int) v.get(i + 1);
                OptimalRate opt = adj[m][n];

                System.out.printf("%.4f %s = %.4f %s (%s)\n", stake, currencies[m], stake *= opt.getValue(), currencies[n], opt.getBankName());
//                System.out.printf("%10.5f %s = %10.5f %s (%s)\n", stake, currencies[m], stake *= Math.exp(-opt.getValue()), currencies[n], opt.getName());
            }
        });
    }

}

