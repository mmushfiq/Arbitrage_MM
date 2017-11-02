package az.mm.arbitrage.factory;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public interface Arbitrage {
    String[] currencies = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};
    void start(Data data);
    
}
