package az.mm.arbitrage.test;

import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author MM
 */
public class BellmanFordCormen2 {
    private double[] dist;
    private int[] p;
    private static double[][] adj;
//    private static int[][] adj;
    private static boolean hasNegativeCycle = false;
//  int INF = Integer.MAX_VALUE;
    int INF = 999;
    private static int vertex = 6;
    private static int source = 0;
    private int temp;
    
    private double startProfit = 1000;
    private String[] cur = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};
    
    
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

    }
    
    
    private void initializeAdj() {
            adj = new double[][]{
                {1,       0.5886,   0.5076,   0.4513,   35.0877,  1.994     },
                {1.697,	  1,	    0.8528,   0.7622,   59.2632,  3.35      },
                {2.035,	  1.1964,   1,	      0.9049,   69.3895,  3.996     },
                {2.223,	  1.3061,   1.0823,   1,	75.4807,  4.3749    },
                {0.0293,  0.0172,   0.0144,   0.0131,   1,	  0.0563    },
                {0.49,	  0.2882,   0.2402,   0.2182,   16.4983,  1         }
            };

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
    

    public void BellmanFord() {
        initializeSingleSource();
        for (int k = 1; k <= vertex; k++) {
            for (int u = 0; u < vertex; u++) {
                for (int v = 0; v < vertex; v++) {
                    relax(u, v);
                }
            }
            checkNegativeCycle();
        }
//        checkNegativeCycle();
    }
    
    public void initializeSingleSource() {
        dist = new double[vertex];
        p = new int[vertex];
        for (int i = 0; i < vertex; i++) {
            dist[i] = Double.MAX_VALUE;
            p[i] = -1;
        }
        dist[source] = 0;
    }

    public void relax(int u, int v) {
        double weight = adj[u][v];
        if (weight != INF && dist[v] > dist[u] + weight) {
            dist[v] = dist[u] + weight;
            p[v] = u;
        }
    }
    
    private void checkNegativeCycle() {
        for (int u = 1; u < vertex; u++) {
            for (int v = /*0*/2; v < vertex; v++) {
                double weight = adj[u][v];
                if (dist[v] > dist[u] + weight) {
                    hasNegativeCycle = true;
                    System.out.print("\nNegative cycle detected! path: ");
                    printNegativeCycle(v);
                    System.out.println("");
//                    return;
                }
            }
        }
    }
    
    private void printNegativeCycle(int v){
        printNegativeCycle(v, 0);
        result *= Math.exp(-adj[v][source]);
        System.out.print(result);
        System.out.print(" "+cur[source]);
    }
    
    double result = 1000;
    private void printNegativeCycle(int v, int count){
//        System.out.println("v="+v);
        if(v==source){
            System.out.print(startProfit+" "+cur[v]);
            System.out.print(" -> ");
            return;
        }
        if(count == 0) temp = v;
//        if(count <= vertex) //prevent StackOverflow
        if(count>0 && temp == v){
            System.out.println("temp==v"); 
            return;
        }  
        else{
            printNegativeCycle(p[v], ++count);
        }
       
//        System.out.print("adj["+p[v]+"]["+v+"]  ");
//        System.out.print(v+"("+cur[v]+") -> ");
        result *= Math.exp(-adj[p[v]][v]);
        System.out.print(result);
        System.out.print(" "+cur[v]+" -> ");
    }
       
    /*chox qeribedi, bu kod metoddan chole compile xetasi vermir, kodu chalishdirdiqda da main metod yoxdur xetasi verir..
        StdOut.printf("%10.5f %s ", stake, name[e.from()]);
        stake *= Math.exp(-e.weight());
        StdOut.printf("= %10.5f %s", stake, name[e.to()]);
    */
    
    
    
    private void printPath(int v){
        printNegativeCycle(v, 0);
    }
    
    
    
    private void convertAdjToLog() {
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j < vertex; j++) {
                adj[i][j] = -Math.log(adj[i][j]);
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
                System.out.print(adj[i][j]+" \t");
            }
             System.out.println("");
        }
    }

}

//
//
//class Edge {
//
//    int u, v;
//    double weight;
//}
