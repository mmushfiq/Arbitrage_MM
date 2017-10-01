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
