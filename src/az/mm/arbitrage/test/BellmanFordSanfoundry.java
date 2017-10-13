package az.mm.arbitrage.test;

import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author MM
 */
public class BellmanFordSanfoundry {

}

class BellmanFord {

    private double d[];
    private int v;
    public static final int MAX_VALUE = 999;

    public BellmanFord(int v) {
        this.v = v;
        d = new double[v + 1];
    }

    public void BellmanFordEvaluation(int source, double w[][]) {
        for (int node = 1; node <= v; node++) {
            d[node] = MAX_VALUE;
        }

        d[source] = 0;
        for (int k = 1; k <= v - 1; k++) {
            for (int i = 1; i <= v; i++) {
                for (int j = 1; j <= v; j++) {
                    if (w[i][j] != MAX_VALUE) {
                        if (d[j] > d[i] + w[i][j]) {
                            d[j] = d[i] + w[i][j];
                        }
                    }
                }
            }
        }

        int count=1;
        for (int i = 1; i <= v; i++) {
            Stack<Integer> cycle = new Stack<>();
            for (int j = 1; j <= v; j++) {
                if (w[i][j] != MAX_VALUE) {
                    cycle.push(j);
                    if (d[j] > d[i] + w[i][j]) {
                        System.out.printf("d[%d] > d[%d] + w[%d][%d](%f)\t", j, i, i, j, w[i][j]);
                        System.out.print(count++ + ". The Graph contains negative egde cycle: ");
                        
                        for(int a: cycle){
                            System.out.print(a + " ");
                        }
                        System.out.println("");
                    }
                }
            }
        }

        for (int vertex = 1; vertex <= v; vertex++) {
            System.out.println("distance of source  " + source + " to " + vertex + " is " + d[vertex]);
        }
    }

    public static void main(String... arg) {
        int v = 0;
        int s;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of vertices");
        v = scanner.nextInt();

        double w[][] = new double[v + 1][v + 1];
        System.out.println("Enter the adjacency matrix");
        for (int i = 1; i <= v; i++) {
            for (int j = 1; j <= v; j++) {
//                w[i][j] = scanner.nextDouble();
                w[i][j] = -Math.log(scanner.nextDouble());
                
                if (i == j) {
                    w[i][j] = 0;
                    continue;
                }
                if (w[i][j] == 0) {
                    w[i][j] = MAX_VALUE;
                }
            }
        }

        System.out.println("Enter the source vertex");
        s = scanner.nextInt();
        
        System.out.println("\nw array:");
        for (int i = 1; i <= v; i++) {
            for (int j = 1; j <= v; j++) {
                System.out.print(w[i][j]+"\t");
            }
            System.out.println("");
        }

        BellmanFord bellmanford = new BellmanFord(v);
        bellmanford.BellmanFordEvaluation(s, w);
        scanner.close();
    }
}



/*
1	0.5879	0.495	34.3643	2.0317	0.4548
1.701	1	0.8421	58.4091	3.4521	0.774
2.0204	1.1873	1	69.8224	4.0973	0.9189
0.0291	0.0171	0.0143	1	0.0587	0.0132
0.4922	0.2896	0.244	17.0343	1	0.2242
2.1988	1.292	1.0883	75.9881	4.4602	1

negative value:
0.0	0.5311984135739278	0.7031975164134467	-3.537018234804052	-0.7088728810214276	0.7878975171057561	
-0.5312163134137248	0.0	0.1718565069461906	-4.067471699613016	-1.2389827415153856	0.2561834053924099	
-0.7032955116117604	-0.17168182168960952	0.0	-4.245954875190955	-1.410328220194436	0.08457797647529774	
3.5370171048046903	4.0686768154735224	4.247495741716276	0.0	2.8353155521480864	4.327538449389812	
0.7088701410251815	1.2392546184705757	1.410587053688935	-2.835228958398956	0.0	1.4952167683440776	
-0.7879117570043682	-0.25619140536041013	-0.08461684571987896	-4.330576749079199	-1.4951936080758808	0.0
*/