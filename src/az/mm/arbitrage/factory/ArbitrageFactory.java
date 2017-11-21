package az.mm.arbitrage.factory;

import az.mm.arbitrage.bellmanford.mine.BellmanFordArbitrage;
import az.mm.arbitrage.bellmanford.princeton.modify.PrincetonBellmanFordArbitrage;
import az.mm.arbitrage.permutation.PermutationArbitrage;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class ArbitrageFactory extends AbstractFactory{
    
    @Override
    public Arbitrage getArbitrage(int n){
         switch (n) {
            case 1:  return new PrincetonBellmanFordArbitrage();      
            case 2:  return new BellmanFordArbitrage();   
            case 3:  return new PermutationArbitrage();      
            default: throw new IllegalArgumentException("Incorrect number! Please enter the number between 1 and 3.");
        }
    }

    @Override
    public Data getData(int type) {
        return null;
    }
}
