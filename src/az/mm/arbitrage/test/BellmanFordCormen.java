package az.mm.arbitrage.test;

import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author MM
 */
public class BellmanFordCormen {
    private double[] dist;
    private int[] p;
//    private static double[][] adj;
    private static int[][] adj;
    private static boolean hasNegativeCycle = false;
    

    
    
    public static void main(String[] args) {
        String[] cur = {"AZN", "USD", "EUR", "GBP"};
        int vertex = 4;
        int source = 0;

        BellmanFordCormen b = new BellmanFordCormen();
        b.initializeAdj(vertex);
//        b.convertAdjToLog(adj, vertex);
        b.BellmanFord(vertex, source); 
        
        b.printArr(adj);
        
        System.out.println(Arrays.toString(b.dist));
        System.out.println(Arrays.toString(b.p));
        
        if (!hasNegativeCycle) {
            for (int j = 1; j < vertex; j++) {
                System.out.print("\ndistance of source  " + source + " to " + j + " is " + b.dist[j] + ",   path: ");
                b.printPath(j);
            }
        }

    }
    
    
    private void initializeAdj(int vertex) {
    //        adj = new double[][]{
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
        
        
        //http://algs4.cs.princeton.edu/lectures/44DemoBellmanFord.pdf - bu linkdeki numuneni tetbiq edib test edecem..
        int INF = Integer.MAX_VALUE;
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
        adj = new int[][]{
                {INF,   5,    4,    INF,  },
                {INF,   INF,  INF,  3,    },
                {INF,   -6,   INF,  INF,  },
                {INF,   INF,  2,    INF,  },
            };
        
    }
    

    public void BellmanFord(int vertex, int source) {
        initializeSingleSource(vertex, source);
        for (int k = 1; k <= vertex; k++) {
            for (int u = 0; u < vertex; u++) {
                for (int v = 0; v < vertex; v++) {
                    relax(u, v);
                }
            }
        }
        checkNegativeCycle(vertex, adj);
    }
    
    public void initializeSingleSource(int vertex, int source) {
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
        if (weight != Integer.MAX_VALUE && dist[v] > dist[u] + weight) {
            dist[v] = dist[u] + weight;
            p[v] = u;
        }
    }
    
    private void checkNegativeCycle(int vertex, double[][] adj) {
        for (int u = 0; u < vertex; u++) {
            for (int v = 0; v < vertex; v++) {
                double weight = adj[u][v];
                if (dist[v] > dist[u] + weight) {
                    System.out.print("\nFound negative cycle! path: ");
                    printNegativeCycle(v);
                    System.out.println("");
                }
            }
        }
    }
    
    private void checkNegativeCycle(int vertex, int[][] adj) {
        for (int u = 0; u < vertex; u++) {
            for (int v = 0; v < vertex; v++) {
                double weight = adj[u][v];
                if (dist[v] > dist[u] + weight) {
                    hasNegativeCycle = true;
                    System.out.print("\nFound negative cycle! path: ");
                    printNegativeCycle(v);
                    System.out.println("");
                    return;
                }
            }
        }
    }
    
    private void printNegativeCycle(int v){
//        System.out.println("v="+v);
        if(v==0){
            System.out.print(v+" -> ");
        return;
        } 
        printNegativeCycle(p[v]);
        System.out.print(v+" -> ");
    }
    
    private void printPath(int v){
        printNegativeCycle(v);
    }
    
    
    
    private void convertAdjToLog(double[][] adj, int vertex) {
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j < vertex; j++) {
                adj[i][j] = -Math.log(adj[i][j]);
            }
        }
    }


    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
    
    
    private void printArr(double[][] adj){
         for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
//                System.out.printf("%f ", adj[i][j]);
                System.out.print(adj[i][j]+" \t");
            }
             System.out.println("");
        }
    }
    
    private void printArr(int[][] adj){
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
