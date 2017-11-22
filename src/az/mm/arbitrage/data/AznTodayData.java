package az.mm.arbitrage.data;

import az.mm.arbitrage.exceptionHandler.ExceptionHandler;
import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class AznTodayData extends Data {
    private int dataId;

    public AznTodayData(int dataId) {
        this.dataId = dataId;
    }
      

    @Override
    public List<Bank> getBankList() {

        List<Bank> aznTodayBankList = new ArrayList<>();
        Bank bank;
        try {
            Document doc = Jsoup.connect("http://azn.today/").get();
            Elements thead = doc.select("#basic-mezenne > thead th");
            Elements tbody = doc.select("#basic-mezenne > tbody tr");

            for (Element e : tbody) {
                e.select("small").remove();
                String name = e.select("td:nth-child(1) > h4").text();

                double curRate[] = new double[10];
                for (int i = 0; i < curRate.length; i++) 
                    try {
                        curRate[i] = Double.parseDouble(e.select("td:nth-child(" + (i + 2) + ")").text());
                    } catch (NumberFormatException ex) {
                        curRate[i] = -1;
                    } 

                bank = new Bank(name, curRate[0], curRate[1], curRate[2], curRate[3], curRate[4], curRate[5], curRate[8], curRate[9], curRate[6], curRate[7]);  // indekslerin yeri sehv idi deyisdirildi
                aznTodayBankList.add(bank);
            }

        } catch (IOException ex) {
            ExceptionHandler.catchMessage(this, new Object(){}.getClass().getEnclosingMethod().getName(), ex);
        }
        
        return aznTodayBankList;
    }

    @Override
    public int getDataId() {
        return dataId;
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }
    
}
