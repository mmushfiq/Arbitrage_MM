package az.mm.arbitrage.bellmanford.mine;

import az.mm.arbitrage.cache.DataCache;
import az.mm.arbitrage.data.ExcelData;
import az.mm.arbitrage.exceptionHandler.ExceptionHandler;
import az.mm.arbitrage.factory.*;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.util.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class BellmanFordArbitrageCopy implements Arbitrage {
    private double[] dist;         // distance array
    private int[] p;               // predecessor array
    private OptimalRate adj[][];   // adjency matrix
    private final int INF = Integer.MAX_VALUE;
    private int vertex, source, number;
    private List<Integer> cycle;
    private Set<List> cycleList;
    private String currencies[];
    

    public BellmanFordArbitrageCopy() {
        cycleList = new LinkedHashSet();
    }
    
    public static void main(String[] args) {
        BellmanFordArbitrageCopy b = new BellmanFordArbitrageCopy();
        b.start(new ExcelData(1));
    }
    

    @Override
    public void start(Data data) {
        currencies = new String[]{"U", "P", "Y", "C", "S"};
        adj = getOptRates(data, currencies);
        vertex = adj.length;
        source = 0;
        callBellmanFord();
    }
    
    public OptimalRate [][] getOptRates(Data data, String[] cur) {
        OptimalRate R[][] = new OptimalRate[cur.length][cur.length];

        double[] arr = {1.0,    1.631,  0.669,  0.008,   0.686,
                        0.613,  1.0,    0.411,  0.005,   0.421,
                        1.495,  2.436,  1.0,    0.012,   1.027,
                        120.5,  197.4,  80.82,  1.0,     82.91,
                        1.459,  2.376,  0.973,  0.012,   1.0
                        };
        
     double[] negArr = {0.0,    0.489,  -0.402,  -4.791,   -0.378,
                        -0.489,  0.0,   -0.891,  -5.278,   -0.865,
                        0.402,  0.89,    0.0,    -4.391,   0.027,
                        4.791,  5.285,   4.392,   0.0,     4.418,
                        0.378,  0.865,  -0.027,  -4.415,   0.0
                        };
     
     /*
            U              P              Y               C             S                             
    U      -0.0000 ->     -0.4892 ->       0.4020 ->        4.8283 ->      0.3769 ->                     
    P       0.4894 ->     -0.0000 ->       0.8892 ->        5.2983 ->      0.8651 ->                     
    Y      -0.4021 ->     -0.8904 ->      -0.0000 ->        4.4228 ->     -0.0266 ->                     
    C      -4.7916 ->     -5.2852 ->      -4.3922 ->       -0.0000 ->     -4.4178 ->                     
    S      -0.3778 ->     -0.8654 ->       0.0274 ->        4.4228 ->     -0.0000 ->    
     
     */
        
        int k=0;
        for (int i = 0; i < R.length; i++) 
            for (int j = 0; j < R.length; j++) {
//                R[i][j] = new OptimalRate("", arr[k++]);
//                R[i][j] = new OptimalRate("", -Math.log(negArr[k++]));
                R[i][j] = new OptimalRate("", negArr[k++]);
            }

        data.checkAndPrintArr(data, R, cur);
        return R.clone();
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
    
    
    private void initializeSingleSource() {
        dist = new double[vertex];
        p = new int[vertex];
        for (int i = 0; i < vertex; i++) {
            dist[i] = INF;
            p[i] = -1;
        }
        dist[source] = 0;
    }
    

    private void relax(int u, int v) {
        if (u == v) return;
//        double weight = -Math.log(adj[u][v].getValue());
        double weight = adj[u][v].getValue();
        if (weight != INF /*&& dist[u] != INF*/ && (dist[v] > dist[u] + weight)) {   //if(d[u] != INFINITY && d[v] > d[u] + w) - lazim olsa bu sherti arashdirmaq mene lazimdi ya yox.
            dist[v] = dist[u] + weight;
            p[v] = u;
        }
    }
    
    /**
     * En chox vaxtimi alan qraflarda butun neqativ cycle-lerin tapilmasi oldu,
     * chunki xeyli mendeden arashdirdim, konkret ele bir menbe chixmadi qarshima
     * ki, orda qeyd olunsun ki, bu mumkundur. Princeton numunesinde de ancaq bir 
     * negative cycle gosterilir. Xeyli arashdirmalardan sonra bunu oz mentiqimle
     * improvizasiya elemishem, amma yene de tam duzgun men isteyen neticeni vermir.
     * 0.01, 0.001 ferqe gore > sherti odenir ve onu negative cycle kimi 
     * qebul edir. Ola biler tam ededlerle duzgun ishlesin, amma double ededlerin
     * loqarifmini alib muqayise aparanda ola bilsin ki, arada suruhsmeler bash verir
     */
    private boolean hasNegativeCycle() {
        for (int u = 0; u < vertex; u++) 
            for (int v = 0; v < vertex; v++) {
                if(u == v) continue;
//                double weight = -Math.log(adj[u][v].getValue());
                double weight = adj[u][v].getValue();
//                if (dist[v] > dist[u] + weight)
//                    findNegativeCyclePath(v);
                
//                if (round(dist[v]) > round(dist[u] + weight)){
                if (dist[v] > dist[u] + weight){
                    System.out.print("\ndist["+v+"]("+dist[v]+") > dist["+u+"]("+dist[u]+") + weight("+weight+") --> "+ (dist[u] + weight));
//                    System.out.print("\ndist["+v+"]("+round(dist[v])+") > dist["+u+"]("+round(dist[u])+") + weight("+round(weight)+") --> "+ (round(dist[u] + weight)));
                    findNegativeCyclePath(v);
                }
                    
                
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
                int from = (int) v.get(i);
                int to = (int) v.get(i + 1);
                OptimalRate opt = adj[from][to];

                System.out.printf("%10.4f %s = %10.4f %s (%s)\n", stake, currencies[from], stake *= opt.getValue(), currencies[to], opt.getBankName());
            }
        });
    }
    
    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

}

