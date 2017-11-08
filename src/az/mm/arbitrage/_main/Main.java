package az.mm.arbitrage._main;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.factory.AbstractFactory;
import az.mm.arbitrage.factory.Arbitrage;
import az.mm.arbitrage.factory.FactoryProducer;
import java.util.Scanner;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nSelect data: \n \t 1 - Excel, 2 - AznToday, 3 - AniMezenne, 4 - Json");
            int type = sc.nextInt();
            AbstractFactory dataFactory = FactoryProducer.getFactory("DATA");
            Data data = dataFactory.getData(type);

            System.out.println("Select algorithm: \n \t 1 - Princeton Bellman-Ford, 2 - Bellman-Ford, 3 - Permutation");
            int algorithm = sc.nextInt();

            AbstractFactory arbitrageFactory = FactoryProducer.getFactory("ARBITRAGE");
            Arbitrage arbitrage = arbitrageFactory.getArbitrage(algorithm);
            arbitrage.start(data);
            
            System.out.println("------------------end------------------------");
        }
    }
    
}
