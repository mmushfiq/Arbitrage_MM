package az.mm.arbitrage.bellmanford.princeton.modify;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.factory.Arbitrage;
import az.mm.arbitrage.model.OptimalRate;
import edu.princeton.cs.introcs.StdOut;

public class PrincetonBellmanFordArbitrage implements Arbitrage {

    @Override
    public void start(Data data) {
        int V = currencies.length;

        // create complete network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);      
        OptimalRate[][] R = data.getOptimalRatesAdjencyMatrix(data.getBankList(), currencies);
          for (int v = 0; v < V; v++) 
            for (int w = 0; w < V; w++) {
                double rate = R[v][w].getValue();
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate), R[v][w].getBankName());
                G.addEdge(e);
            }

        
        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                StdOut.printf("%10.5f %s ", stake, currencies[e.from()]);
                stake *= Math.exp(-e.weight());
                StdOut.printf("= %10.5f %s", stake, currencies[e.to()]);
                System.out.printf(" (%s)\n", e.getBankName());  
            }
        } else 
            StdOut.println("No arbitrage opportunity");
        
    }

}
