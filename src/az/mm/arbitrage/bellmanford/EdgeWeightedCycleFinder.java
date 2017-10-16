package az.mm.arbitrage.bellmanford;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class EdgeWeightedCycleFinder {

    private EdgeWeightedDigraph G;
    private boolean[] onStack;
    private DirectedEdge[] edgeTo;
    private boolean[] marked;
    private boolean hasCycle;
    private Stack<DirectedEdge> cycle;

    public EdgeWeightedCycleFinder(EdgeWeightedDigraph G) {
        this.G = G;
        onStack = new boolean[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        marked = new boolean[G.V()];
        for (int i = 0; i < G.V(); ++i) {
            if (!marked[i] && !hasCycle()) {
                dfs(i);
            }
        }
    }

    private void dfs(int v) {
        marked[v] = true;
        onStack[v] = true;
        for (DirectedEdge e : G.adj(v)) {
            if (hasCycle) {
                break;
            }
            int w = e.to();
            if (onStack[w]) {
                hasCycle = true;
                cycle = new Stack<DirectedEdge>();
                int p = v;
                cycle.push(e);
                while (p != w) {
                    cycle.push(edgeTo[p]);
                    p = edgeTo[p].from();
                }
            } else {
                if (!marked[w]) {
                    edgeTo[w] = e;
                    dfs(w);
                }
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }
}
