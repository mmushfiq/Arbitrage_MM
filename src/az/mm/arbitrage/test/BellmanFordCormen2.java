package az.mm.arbitrage.test;

import az.mm.arbitrage.data.AniMezenneData;
import az.mm.arbitrage.data.AznTodayData;
import az.mm.arbitrage.data.Data;
import az.mm.arbitrage.data.ExcelData;
import az.mm.arbitrage.model.OptimalRate;
import az.mm.arbitrage.princeton.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author MM
 */
public class BellmanFordCormen2 {
    private double[] dist;
    private int[] p;
//    private static double[][] adj;
//    private static int[][] adj;
    private OptimalRate adj[][];
    
    private static boolean hasNegativeCycle = false;
//  int INF = Integer.MAX_VALUE;
    int INF = 999;
    private static int vertex = 6;
    private static int source = 0;
    private int temp;
    
    private double startProfit = 1000;
    private String[] cur = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};
    private double result;
    private Map<Integer, List> listCycle; 
    private List<Integer> list;
    static int number;
    
    private Set<List> setCycle;
    

    public BellmanFordCormen2() {
        listCycle = new LinkedHashMap<>();
        setCycle = new LinkedHashSet();
    }
    
    
    public static void main(String[] args) {

        BellmanFordCormen2 b = new BellmanFordCormen2();
        b.initializeAdj();
        b.convertAdjToLog();
        b.BellmanFord(); 
        
        b.printArr();
        
        System.out.println("\ndistance arr: "+Arrays.toString(b.dist));
        System.out.println("\npath arr: "+Arrays.toString(b.p));
        
        if (!hasNegativeCycle) {
            for (int j = 1; j < vertex; j++) {
                System.out.print("\ndistance of source  " + source + " to " + j + " is " + b.dist[j] + ",   path: ");
                b.printPath(j);
            }
        }

        
        System.out.println("\n"+b.listCycle);
        
//        b.calculate();
    }
    
    
    public void initializeSingleSource() {
        dist = new double[vertex];
        p = new int[vertex];
        for (int i = 0; i < vertex; i++) {
            dist[i] = INF;
            p[i] = -1;
        }
        dist[source] = 0; //chox guman ki, bunun qiymeti sona qeder deyishmemelidi, for.da v.ni 0 verende deyishir, 0 vermeyende de source.den birbasha negative cycle gostermir.. arashdirmaq lazimdi..
    }

    public void BellmanFord() {
        initializeSingleSource();
        for (int k = 1; k < vertex; k++) { //iterate (vertex-1)
//            System.out.println("count relax: "+k);
            for (int u = 0; u < vertex; u++) {
                for (int v = 0; v < vertex; v++) { //probably v haven't to be 0
                    relax(u, v);
                }
            }
//            checkNegativeCycle();
//            checkArbitrage();
        }
        checkNegativeCycle();
//        checkArbitrage();
        checkArbitrage2();
        System.out.println("\n-----------with set-------------------\n");
        checkArbitrage3Set();
    }
    

    public void relax(int u, int v) {
        if(u==v) return;
//        System.out.println("edge "+u+" -> "+v);
        double weight = adj[u][v].getValue();
//        double weight = -Math.log(adj[u][v].getValue());
        if (weight != INF /*&& dist[u] != INF*/ && (dist[v] > dist[u] + weight)) {   //if(d[u] != INFINITY && d[v] > d[u] + w) - lazim olsa bu sherti arashdirmaq mene lazimdi ya yox.
            dist[v] = dist[u] + weight;
            p[v] = u;
        }
//        else if(dist[v] == dist[u] + weight){
//            p[v] = u;
//        }
    }
    
    private void checkNegativeCycle() {
        for (int u = 0; u < vertex; u++) {
            for (int v = 0; v < vertex; v++) {
                if(u==v) continue;
                double weight = adj[u][v].getValue();
//                double weight = -Math.log(adj[u][v].getValue());
                if (dist[v] > dist[u] + weight) {
                    hasNegativeCycle = true;
                    System.out.print("\ndist["+v+"]("+dist[v]+") > dist["+u+"]("+dist[u]+") + weight("+weight+")");
                    System.out.print("\nNegative cycle detected! path: ");
//                    printNegativeCycle(v);
//                    printNegativeCycle2(v);
//                    printPath3(source, v);
                    printNegativeCycle4(v);
                    System.out.println("");
//                    return;
                }
            }
        }
    }
    
    
    private void checkArbitrage(){
        number=0;
        System.out.println("\n---------------");
        System.out.println("map: "+listCycle);
        
        listCycle.forEach((k, v) -> {
            System.out.printf("\nArbitrage %d: \n", ++number);
            result = 1000;
            if(v.size()==1) return;
             
            Collections.reverse(v);
            v.add(v.get(0));
            System.out.println(v);
            for(int i=0; i<v.size()-1; i++){
                int m = (int)v.get(i);
                int n = (int)v.get(i+1);
                OptimalRate opt = adj[m][n];
                
//                System.out.print(result + " " + cur[m]+" = ");
//                result *= opt.getValue();
//                System.out.println(result + " " + cur[n]+" ("+opt.getName()+")");
                
                System.out.printf("%.4f %s = %.4f %s (%s)\n", result, cur[m], result *= opt.getValue(), cur[n], opt.getName());
                
//                System.out.println("---------------------------");
                
//                for(ArbitrageModel a: value)
//                    System.out.printf("%.4f %s = %.4f %s (%s)\n", a.getFirstResult(), a.getFromCur(), a.getLastResult(), a.getToCur(), a.getBankName());
            }
        });
        
    }
    
    
    private void checkArbitrage2(){
        number=0;
        System.out.println("\n---------------");
        System.out.println("map: "+listCycle);
        
        listCycle.forEach((k, v) -> {
            System.out.printf("\nArbitrage %d: \n", ++number);
            result = 1000;
//            if(v.size()==1) return;
             
//            Collections.reverse(v);
//            v.add(v.get(0));
            System.out.println(v);
            for(int i=0; i<v.size()-1; i++){
                int m = (int)v.get(i);
                int n = (int)v.get(i+1);
                OptimalRate opt = adj[m][n];
                
//                System.out.print(result + " " + cur[m]+" = ");
//                result *= opt.getValue();
//                System.out.println(result + " " + cur[n]+" ("+opt.getName()+")");
                
                System.out.printf("%.4f %s = %.4f %s (%s)\n", result, cur[m], result *= opt.getValue(), cur[n], opt.getName());
                
//                System.out.println("---------------------------");
                
//                for(ArbitrageModel a: value)
//                    System.out.printf("%.4f %s = %.4f %s (%s)\n", a.getFirstResult(), a.getFromCur(), a.getLastResult(), a.getToCur(), a.getBankName());
            }
        });
        
    }
    
    
    private void checkArbitrage3Set(){
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
        
        number=0;
        System.out.println("\n---------------");
        System.out.println("set: "+setCycle);
        
        setCycle.forEach(v -> {
            System.out.printf("\nArbitrage %d: \n", ++number);
            result = 1000;
            System.out.println(v);
            for (int i = 0; i < v.size() - 1; i++) {
                int m = (int) v.get(i);
                int n = (int) v.get(i + 1);
                OptimalRate opt = adj[m][n];

//                System.out.printf("%.4f %s = %.4f %s (%s)\n", result, cur[m], result *= opt.getValue(), cur[n], opt.getName());
                System.out.printf("%10.5f %s = %10.5f %s (%s)\n", result, cur[m], result *= Math.exp(-opt.getValue()), cur[n], opt.getName());

            }
        });
    }
    

    private void printNegativeCycle(int v){
        
        System.out.println("v="+v);
        result = 1000;
        printNegativeCycle(v, 0);
        result *= Math.exp(-adj[v][source].getValue());
        System.out.print(result+" "+cur[source]);
//        listCycle.put(number++, list);
        listCycle.put(v, list); //bu duplicateleri aradan qaldirir, ama hele ki commente atim axirda acharam yene..
    }
    

    private void printNegativeCycle(int v, int count){
        list = new ArrayList<>();
//        System.out.println("v="+v);
        if(v == source){
//            sb.append(v+" ");
            list.add(v);
            System.out.print(v+" -> ");
            System.out.print(startProfit+" "+cur[v] + " -> ");
            return;
        }
//        if(count == 0){ 
//            temp = v;
//            System.out.println("count==0 sertine girdi, temp: "+temp);
//        }
////        if(count <= vertex) //prevent StackOverflow
//        if(count>0 && temp == v){
//            System.out.println("temp==v"); 
//            return;
//        }  
//        else{
//            printNegativeCycle(p[v], ++count);
//        }
        
        if(list.contains(v)){
            System.out.println("contain: "+v);
            list.add(v);
            return;
        } else{
            list.add(v);
            printNegativeCycle(p[v], ++count);
            System.out.print(v + " -> ");
        }
       
//        System.out.print("adj["+p[v]+"]["+v+"]  ");
//        System.out.print(v+"("+cur[v]+") -> ");
        
        result *= Math.exp(-adj[p[v]][v].getValue());
        System.out.print(result+" "+cur[v]+" -> ");
    }
    
    private void printNegativeCycle2(int v) {
        System.out.println("printNegativeCycle2 v: " + v);
        int y = v;
//        for (int i=0; i<n; ++i)
//            y = p[y];

        int c=0;
        Stack<Integer> path = new Stack<>();
        for (int cur = y; c++ < p.length; cur = p[cur]) {
            path.push(cur);
            if ((cur == y && path.size() > 1) /*|| cur == 0*/) {
                break;
            }
        }
//        reverse (path.begin(), path.end());

        System.out.println("Stack Negative cycle: ");
//        for (int i=0; i<path.size(); ++i)
//            cout << path[i] << ' ';

    }
       
    /*chox qeribedi, bu kod metoddan chole compile xetasi vermir, kodu chalishdirdiqda da main metod yoxdur xetasi verir..
        StdOut.printf("%10.5f %s ", stake, name[e.from()]);
        stake *= Math.exp(-e.weight());
        StdOut.printf("= %10.5f %s", stake, name[e.to()]);
    */
    
    
    
    private void printPath(int v){
        printNegativeCycle(v, 0);
    }
    
    /*
    * https://mitpress.mit.edu/sites/default/files/titles/content/Intro_to_Algo_Selected_Solutions.pdf - page 65
    
    PRINT-PATH (G, s, v) 
    1 if v == s
    2   print s
    3 elseif v.p == NIL
    4   print “no path from” s “to” v “exists”
    5 else PRINT-PATH (G, s, v.p)
    6   print v
    
    */
    
    
    private void printPath3(int s, int v){
        if(v == s){
            System.out.print(s + " ");
            return;
        }
//        else if(p[v] == -1)
//            System.out.println("no path from s to v exists");
//        else 
            printPath3(s, p[v]);
        System.out.print(v+" ");
    }
    
    
    private void printNegativeCycle4(int v) {
        if(v==0) return;
        System.out.println("v="+v);
        boolean visited[] = new boolean[p.length];

        Stack<Integer> path = new Stack<>();
        for (int cur = v; ; cur = p[cur]) {
            path.push(cur);
//            if(cur == 0) break;
//            if(!visited[cur]) visited[cur]=true;
//            else break;
            
            if (cur == 0 || visited[cur]) break;
            else visited[cur] = true;
            
            
            
        }

        System.out.println("Stack Negative cycle: ");

        list = new ArrayList();
        for (Integer i : path) {
            System.out.print(i + " ");

            if(list.contains(i)) break;
            list.add(i);
        }
        if(list.size()>1)list.add(list.get(0));
        System.out.println("\nlist: "+list);
        
        listCycle.put(v, list);
        setCycle.add(list);
    }
    
    
    
    private void convertAdjToLog() {
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j < vertex; j++) {
//                adj[i][j] = -Math.log(adj[i][j]);
//                if(i==j){
//                    adj[i][j].setValue(-Math.log(adj[i][j].getValue())); 
//                continue;
//                } 
                adj[i][j].setValue(-Math.log(adj[i][j].getValue())); 
            }
        }
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
//        Data d = new ExcelData();
//        Data d = new AznTodayData();
        Data d = new AniMezenneData();
        adj = d.getOptimalRatesArrayTest(d.getBankList(), cur);
        
//        for(OptimalRate[] opt: adj){
//            for(OptimalRate o: opt){
//                System.out.print(o + " ");
//            }
//            System.out.println("");
//        }
            
        
//            adj = new double[][]{
//                {1,       0.5886,   0.5076,   0.4513,   35.0877,  1.994     },
//                {1.697,	  1,	    0.8528,   0.7622,   59.2632,  3.35      },
//                {2.035,	  1.1964,   1,	      0.9049,   69.3895,  3.996     },
//                {2.223,	  1.3061,   1.0823,   1,	75.4807,  4.3749    },
//                {0.0293,  0.0172,   0.0144,   0.0131,   1,	  0.0563    },
//                {0.49,	  0.2882,   0.2402,   0.2182,   16.4983,  1         }
//            };

//        adj = new double[][]{
//                {1,       0.5886,   0.5076,   0.4513,  },
//                {1.697,	  1,	    0.8528,   0.7622,  },
//                {2.035,	  1.1964,   1,	      0.9049,  },
//                {2.223,	  1.3061,   1.0823,   1,       }
//            };
        
//        adj = new int[][]{
//                {10,   5,   10,   4,  },
//                {-4,	10,  3,   7,  },
//                {20,	11,  10,  -5,  },
//                {22,	1,  10,  10, }
//                
//            };
        
//         adj = new int[][]{
//                {0,   5,   10,  4,  10,  14 },
//                {4,   0,   3,   7,  5,   4 },
//                {6,   11,  0,   5,  3,   4 },
//                {12,  1,   10,  0,  4,   4 },
//                {8,   9,   3,   5,  0,   4 },
//                {4,   1,   2,   6,  7,   0 }
//        };
        
//            adj = new int[][]{
//                {INF,   INF,   INF,  INF,  INF,   INF,  INF,  INF,  INF   },
//                {INF,   INF,   4,  INF,  4,   INF,  INF,  INF,  INF   },
//                {INF,   INF,   INF,  INF,  INF,   INF,  INF,  INF,  INF   },
//                {INF,   3,   INF,  INF,  2,   INF,  INF,  INF,  INF   },
//                {INF,   INF,   INF,  INF,  INF,   -2,  4,  INF,  INF   },
//                {INF,   INF,   3,  INF,  INF,   INF,  -3,  INF,  INF   },
//                {INF,   INF,   INF,  1,  INF,   INF,  INF,  -2,  INF   },
//                {INF,   INF,   INF,  INF,  INF,   2,  INF,  INF,  2   },
//                {INF,   INF,   INF,  INF,  INF,   INF,  -2,  INF,  INF   },
//            };
        
        
        //http://algs4.cs.princeton.edu/lectures/44DemoBellmanFord.pdf - bu linkdeki numuneni tetbiq edib test edecem..
//        adj = new double[][]{
//                {INF,   5,     INF,  INF,  9,    INF,  INF,  8   },
//                {INF,   INF,   12,   15,   INF,  INF,  INF,  4   },
//                {INF,   INF,   INF,  3,    INF,  INF,  11,   INF },
//                {INF,   INF,   INF,  INF,  INF,  INF,  9,    INF },
//                {INF,   INF,   INF,  INF,  INF,  4,    20,   5   },
//                {INF,   INF,   1,    INF,  INF,  INF,  13,   INF },
//                {INF,   INF,   INF,  INF,  INF,  INF,  INF,  INF },
//                {INF,   INF,   7,    INF,  INF,  6,    INF,  INF },
//            };
        
        
        //https://www.dyclassroom.com/graph/detecting-negative-cycle-using-bellman-ford-algorithm - with negative weight
//        adj = new int[][]{
//                {INF,   5,    4,    INF,  },
//                {INF,   INF,  INF,  3,    },
//                {INF,   -6,   INF,  INF,  },
//                {INF,   INF,  2,    INF,  },
//            };
        
    }
    
    private void calculate(){
        System.out.println("\n\n------test----------");
        System.out.print(-Math.log(adj[2][4].getValue()) +" + "+ (-Math.log(adj[4][3].getValue()))+" + "+ (-Math.log(adj[3][2].getValue()))+" = ");
        System.out.println(-Math.log(adj[2][4].getValue()) + (-Math.log(adj[4][3].getValue()))+ (-Math.log(adj[3][2].getValue())));
        
        System.out.print(adj[2][4].getValue() +" * "+ (adj[4][3].getValue())+" * "+ (adj[3][2].getValue())+" = ");
        System.out.println(adj[2][4].getValue() * (adj[4][3].getValue()) * (adj[3][2].getValue()));
        
        System.out.print(-Math.log(adj[2][4].getValue()) +" + "+ (-Math.log(adj[4][3].getValue()))+" + "+ (-Math.log(adj[3][2].getValue()))+" + "+ (-Math.log(adj[2][1].getValue()))+" + "+ (-Math.log(adj[1][2].getValue()))+" = ");
        System.out.println(-Math.log(adj[2][4].getValue()) + (-Math.log(adj[4][3].getValue()))+ (-Math.log(adj[3][2].getValue()))+ (-Math.log(adj[2][1].getValue()))+ (-Math.log(adj[1][2].getValue())));
        
        System.out.print(adj[2][4].getValue() +" * "+ (adj[4][3].getValue())+" * "+ (adj[3][2].getValue())+" * "+ (adj[2][1].getValue())+" * "+ (adj[1][2].getValue())+" = ");
        System.out.println(adj[2][4].getValue() * (adj[4][3].getValue()) * (adj[3][2].getValue()) * (adj[2][1].getValue()) * (adj[1][2].getValue()));
    }

}

//
//
//class Edge {
//
//    int u, v;
//    double weight;
//}
