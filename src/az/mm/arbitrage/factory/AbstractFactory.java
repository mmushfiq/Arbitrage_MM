package az.mm.arbitrage.factory;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public abstract class AbstractFactory {
    public abstract Data getData(int type);
    public abstract Arbitrage getArbitrage(int type);
}
