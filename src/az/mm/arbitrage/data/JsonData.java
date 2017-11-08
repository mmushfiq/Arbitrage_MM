package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author MM
 */
public class JsonData extends Data {
    private int dataId;
    private LocalDate date;
    private String[] currencies = {"USD", "CHF", "GBP", "JPY", "RUB", "TRY", "EUR"};

    public JsonData(int dataId) {
        this.dataId = dataId;
    }
    
    @Override
    public int getDataId() {
        return dataId;
    }
    
    @Override
    public List<Bank> getBankList() {
        return new ArrayList<Bank>();
    }
    
    @Override
    public LocalDate getDate() {
        return date;
    }


//    @Override
    public Map<String, Map<String, OptimalRate>> getOptimalRatesMap(List<Bank> bankList, String baseCurrency, String[] cur) {
        Map<String, Map<String, OptimalRate>> ratesMap = new HashMap();
        getRatesMap().forEach((key, value) -> {
            Map<String, OptimalRate> map = new HashMap();
            value.forEach((k2, v2) -> {
                OptimalRate opt = new OptimalRate();
                opt.setValue(v2);
                map.put(k2, opt);
            });
            ratesMap.put(key, map);
        });

        return ratesMap;
    }

    @Override
    public OptimalRate[][] getOptimalRatesAdjencyMatrix(Data data, String[] cur) {
        
        //https://stackoverflow.com/questions/2265266/convert-hash-map-to-2d-array
     OptimalRate[][] R = new OptimalRate[currencies.length][currencies.length];
         for (int i = 0; i < R.length; i++) 
                for (int j = 0; j < R.length; j++) {
                    
                    if (i == j){
                        R[i][j] = new OptimalRate("", 1.);
                        continue;
                    } 
        
        getRatesMap().forEach((k,v) -> {
            
            System.out.printf("\n%-5s",k);
            v.forEach((k2,v2) -> {
                System.out.printf("->%s: %.4f - %-17s ", k2, v2.getValue(), v2.getBankName());
            });
        });
    }
    }
    
    
 

    public Map<String, Map<String, Double>> getRatesMap() {
        Map<String, Map<String, Double>> ratesMap = new LinkedHashMap();
        JSONObject jsonObject = null;
        
        for (int i = 0; i < currencies.length; i++) {
            jsonObject = readJsonFromUrl("http://api.fixer.io/latest?base=" + currencies[i]);
            Map<String, Double> map = (Map) jsonObject.get("rates");
            ratesMap.put(currencies[i], map);
        }

        if(jsonObject != null) date = LocalDate.parse(jsonObject.get("date").toString());
        
        return ratesMap;
    }
   

    private JSONObject readJsonFromUrl(String url) {
        JSONObject jsonObject = null;
        try (InputStream is = new URL(url).openStream(); 
             BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); ) {
            
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(sb.toString());
            jsonObject = (JSONObject) obj;
        } catch(Exception ex){
            ex.printStackTrace();
        } 
        
        return jsonObject;
    }

}
