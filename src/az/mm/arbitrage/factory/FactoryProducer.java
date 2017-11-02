package az.mm.arbitrage.factory;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class FactoryProducer {

    public static AbstractFactory getFactory(String choice) {
        switch(choice.toUpperCase()){
            case "ARBITRAGE":  return new ArbitrageFactory();
            case "DATA":       return new DataFactory();
            default:           return null;
        }
    }

}
