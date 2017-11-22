package az.mm.arbitrage.bellmanford.princeton.modify;

import az.mm.arbitrage.cache.DataCache;
import az.mm.arbitrage.factory.*;
import az.mm.arbitrage.model.OptimalRate;
import edu.princeton.cs.introcs.StdOut;

/**
 * Bu paketdeki classlar Princeton numunesinin modifikasiya olunmush formalaridir.
 * Orijinal classlar az.mm.arbitrage.princeton paketindedir.
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class PrincetonBellmanFordArbitrage implements Arbitrage {
    
    @Override
    public void start(Data data) {
        String currencies[] = data.getCurrencies();
        int V = currencies.length;

        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);      
        OptimalRate [][] R = DataCache.getAdjencyMatrix(data, currencies);
          for (int v = 0; v < V; v++) 
            for (int w = 0; w < V; w++) {
                double rate = R[v][w].getValue();
                if(rate == -1) continue; //no conversion
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate), R[v][w].getBankName());
                G.addEdge(e);
            }
        
        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            System.out.println("\nArbitrage:");
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                StdOut.printf("%10.5f %s ", stake, currencies[e.from()]);
                stake *= Math.exp(-e.weight());
                StdOut.printf("= %10.5f %s (%s)\n", stake, currencies[e.to()], e.getBankName());
            }
        } else 
            StdOut.println("No arbitrage opportunity");
    }

}
