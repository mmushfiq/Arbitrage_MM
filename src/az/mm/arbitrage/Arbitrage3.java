package az.mm.arbitrage;

import az.mm.arbitrage.data.AniMezenneData;
import az.mm.arbitrage.data.AznTodayData;
import az.mm.arbitrage.data.Data;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.data.ExcelData;
import az.mm.arbitrage.data.JsonData;
import az.mm.arbitrage.model.OptimalRate;

import edu.princeton.cs.introcs.StdOut;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Arbitrage3 {

    private static List<Bank> bankList;
    private Data d;

    // this class cannot be instantiated
    private Arbitrage3() {
    }

    public static void main(String[] args) {
        System.out.println("Choose process number: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        Arbitrage3 arb = new Arbitrage3();
        arb.choice(choice);
    }

    public void choice(int n) {
        switch (n) {
            case 1:
                d = new ExcelData();
                break;
            case 2:
                d = new AznTodayData();
                break;
            case 3:
                d = new AniMezenneData();
                checkAniMezenneOldData();
                return;
            case 4:
                d = new JsonData();
                break;
            default:
                d = new ExcelData();
                break;
        }
        
        bankList = d.getBankList();
        if (d instanceof JsonData) startArbitrageForJsonData();
        else startArbitrage(bankList);
    }

    public void checkAniMezenneOldData() {
        List<Bank> partList = new ArrayList<>();
        Date date = null;

        for (Bank b : d.getBankList()) { //d meselesine bir de baxmaq..
            if (date == null) date = b.getDate();
            
            if (date.equals(b.getDate())) 
                partList.add(b);
            else {
                System.out.println("\n---------------------------------\n" + date);
                startArbitrage(partList);
                partList.clear();
                date = b.getDate();
            }
        }

    }

    public void startArbitrage(List<Bank> bankList) {
        String[] currencies = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};

        Map<String, Map<String, OptimalRate>> ratesMap = d.getOptimalRatesMap(bankList);

        String bankName;

        // V currencies
        int V = currencies.length;
        String[] name = new String[V];

        // create complete network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            name[v] = currencies[v];
            System.out.print(name[v] + "\t");
            Map<String, OptimalRate> map = ratesMap.get(currencies[v]);
            for (int w = 0; w < V; w++) {

                double rate = (v == w) ? 1 : map.get(currencies[w]).getValue();
                if (v == w) {
                    bankName = "";
                } else {
                    bankName = map.get(currencies[w]).getName();
                }
//                System.out.print(rate+"\t\t");
                System.out.printf("%.4f (%s)\t", rate, bankName);
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate), bankName);
                G.addEdge(e);
            }
            System.out.println("");
        }
        System.out.println("");

        callBellmanFord(G, name);
    }
    
    
    public void startArbitrageForJsonData() {
        String[] currencies = {"USD", "CHF", "GBP", "JPY", "RUB", "TRY", "EUR"};
        Map<String, Map<String, Double>> ratesMap = new JsonData().getRatesMap();
        
        // V currencies
        int V = currencies.length;
        String[] name = new String[V];

        // create complete network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            name[v] = currencies[v];
            System.out.print(name[v]+"\t");
            Map<String, Double> map = ratesMap.get(currencies[v]);
            for (int w = 0; w < V; w++) {
                
                double rate = (v == w) ? 1 : map.get(currencies[w]);
                System.out.print(rate+"\t\t");
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
                G.addEdge(e);
            }
            System.out.println("");
        }

        callBellmanFord(G, name);
    }
    
    
    private void callBellmanFord(EdgeWeightedDigraph G, String[] name){
        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                StdOut.printf("%10.5f %s ", stake, name[e.from()]);
                stake *= Math.exp(-e.weight());
                StdOut.printf("= %10.5f %s", stake, name[e.to()]);
                System.out.printf(" (%s)\n", e.getBankName());  //16.09.2017-de elave edildi..
            }
        } else {
            StdOut.println("No arbitrage opportunity");
        }
    }

}
