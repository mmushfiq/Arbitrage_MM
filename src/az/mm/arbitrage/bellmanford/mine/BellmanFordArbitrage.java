package az.mm.arbitrage.bellmanford.mine;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.factory.Arbitrage;
import az.mm.arbitrage.model.OptimalRate;
import az.mm.arbitrage.princeton.Stack;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author MM
 */
public class BellmanFordArbitrage implements Arbitrage {
    private double[] dist; //distance array
    private int[] p;       //precedessor array
    private OptimalRate adj[][];
    
//    private static boolean hasNegativeCycle = false;
//  int INF = Integer.MAX_VALUE;
    int INF = 999;
    private static int vertex = 6;
    private static int source = 0;
    private int number;
    
//    private String[] currency = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};
    private List<Integer> cycle;
    private Set<List> cycleList;
    

    public BellmanFordArbitrage() {
        cycleList = new LinkedHashSet();
    }
    

    @Override
    public void start(Data data) {
        adj = data.getOptimalRatesAdjencyMatrix(data.getBankList(), currencies);
        callBellmanFord();
    }
    

    public void callBellmanFord() {
        initializeSingleSource();
        for (int k = 1; k < vertex; k++) { //iterate (vertex-1)
            for (int u = 0; u < vertex; u++) {
                for (int v = 0; v < vertex; v++) { 
                    relax(u, v);
                }
            }
        }
        checkNegativeCycle();
        
        if (!cycleList.isEmpty()) checkArbitrage();
        else System.out.println("No arbitrage opportunity");
    }
    
    public void initializeSingleSource() {
        dist = new double[vertex];
        p = new int[vertex];
        for (int i = 0; i < vertex; i++) {
            dist[i] = INF;
            p[i] = -1;
        }
        dist[source] = 0; //chox guman ki, bunun qiymeti sona qeder deyishmemelidi, for.da v.ni 0 verende deyishir, 0 vermeyende de source.den birbasha negative cycle gostermir.. arashdirmaq lazimdi.. princeton numunesine baxdim orda da deyishir..
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
    
    private void checkNegativeCycle() {
        for (int u = 0; u < vertex; u++) {
            for (int v = 0; v < vertex; v++) {
                if(u == v) continue;
                double weight = adj[u][v].getValue();
//                double weight = -Math.log(adj[u][v].getValue());
                if (dist[v] > dist[u] + weight) {
//                    hasNegativeCycle = true;
//                    System.out.print("\ndist["+v+"]("+dist[v]+") > dist["+u+"]("+dist[u]+") + weight("+weight+")");
//                    System.out.print("\nNegative cycle detected! path: ");
                    findNegativeCyclePath(v);
//                    System.out.println("");
                }
            }
        }
    }
    

  
    /*chox qeribedi, bu kod metoddan chole compile xetasi vermir, kodu chalishdirdiqda da main metod yoxdur xetasi verir..
        StdOut.printf("%10.5f %s ", stake, name[e.from()]);
        stake *= Math.exp(-e.weight());
        StdOut.printf("= %10.5f %s", stake, name[e.to()]);
    */
    

    
    private void findNegativeCyclePath(int v) {
        if(v == 0) return;
        System.out.println("v="+v);
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
        

        System.out.println("Stack Negative cycle: ");

        cycle = new ArrayList();
        for (Integer i : path) {
            System.out.print(i + " ");

            if(cycle.contains(i)) break;
            cycle.add(i);
        }
        
        if (cycle.size() > 1) cycle.add(cycle.get(0));
        System.out.println("\nlist: "+cycle);
        
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
        
        number = 1;
        System.out.println("\n---------------");
        System.out.println("set: "+cycleList);
        
        cycleList.forEach(v -> {
            System.out.printf("\nArbitrage %d: \n", number++);
            double stake = 1000;
            System.out.println(v);
            for (int i = 0; i < v.size() - 1; i++) {
                int m = (int) v.get(i);
                int n = (int) v.get(i + 1);
                OptimalRate opt = adj[m][n];

                System.out.printf("%.4f %s = %.4f %s (%s)\n", stake, currencies[m], stake *= opt.getValue(), currencies[n], opt.getName());
//                System.out.printf("%10.5f %s = %10.5f %s (%s)\n", stake, currencies[m], stake *= Math.exp(-opt.getValue()), currencies[n], opt.getName());

            }
        });
    }
    


/*        
    private void createNegativeWeightedAdjencyMatrix() {
        for (int i = 0; i < vertex; i++) 
            for (int j = 0; j < vertex; j++) 
                adj[i][j].setValue(-Math.log(adj[i][j].getValue())); 
    }


    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
    
    
    private void printArr(){
        System.out.println("");
         for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
//                System.out.printf("%f ", adj[i][j]);
                System.out.print(adj[i][j].getValue()+" \t");
            }
             System.out.println("");
        }
         
        System.out.println("------------------------------------------------");
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                System.out.print(-Math.log(adj[i][j].getValue())+" \t");
            }
             System.out.println("");
        }
    }
    
    
    private void initializeAdj() {
//        String[] cur = {"AZN", "USD", "EUR", "GBP", "RUB","TRY"};
        Data d = new ExcelData();
//        Data d = new AznTodayData();
//        Data d = new AniMezenneData();
        adj = d.getOptimalRatesArrayTest(d.getBankList(), currency); 
    }
    
        
        
    public static void main(String[] args) {

        BellmanFordArbitrage b = new BellmanFordArbitrage();
        b.initializeAdj();
        b.printArr();
        b.createNegativeWeightedAdjencyMatrix();
        b.callBellmanFord(); 
        
//        b.printArr();
        
        System.out.println("\ndistance arr: "+Arrays.toString(b.dist));
        System.out.println("\npath arr: "+Arrays.toString(b.p));
        
    }
*/

}
