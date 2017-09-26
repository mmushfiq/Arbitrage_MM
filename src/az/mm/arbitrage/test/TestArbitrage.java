package az.mm.arbitrage.test;

import az.mm.arbitrage.model.OptimalRate;
import az.mm.arbitrage.data.ExcelData;

/**
 *
 * @author MM
 */
public class TestArbitrage {

    public static void main(String[] args) {
//        ExcelData exc = new ExcelData();
//        OptimalRate opt = exc.getOptimalRates("AZN", "USD");
//        System.out.println("AZN-USD --> " + opt);
//        opt = exc.getOptimalRates("USD", "AZN");
//        System.out.println("USD-AZN --> " + opt);
//        opt = exc.getOptimalRates("USD", "EUR");
//        System.out.println("USD-EUR --> " + opt);
//
//        opt = exc.getOptimalRates("EUR", "USD");
//        System.out.println("EUR-USD --> " + opt);
        
        test();

    }

    public static void test() {
        String[] cur = {"AZN", "USD", "EUR", "GBP", "RUB", "TRY",};
        ExcelData exc = new ExcelData();
        OptimalRate opt;

        for (int i = 0; i < cur.length; i++) {
            System.out.print(cur[i] + "\t");
            for (int j = 0; j < cur.length; j++) {
                if(i == j) {
                    System.out.print(1 + "\t");
                    continue;
                }
                opt = exc.getOptimalRates(cur[i], cur[j], ExcelData.bankList);
                System.out.print(opt.getValue() + "\t");
            }
            System.out.println("");
        }

    }
}
