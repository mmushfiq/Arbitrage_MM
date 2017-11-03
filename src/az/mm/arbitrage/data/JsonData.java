package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
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
    
    @Override
    public List<Bank> getBankList() {
        return new ArrayList<Bank>();
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
 

    public Map<String, Map<String, Double>> getRatesMap() {
        String[] currencies = {"USD", "CHF", "GBP", "JPY", "RUB", "TRY", "EUR"};
        Map<String, Map<String, Double>> ratesMap = new LinkedHashMap();
    
        for (int i = 0; i < currencies.length; i++) {
            JSONObject jsonObject = readJsonFromUrl("http://api.fixer.io/latest?base=" + currencies[i]);
            Map<String, Double> map = (Map) jsonObject.get("rates");
            ratesMap.put(currencies[i], map);
        }

        return ratesMap;
    }

    private JSONObject readJsonFromUrl(String url) {
        JSONObject jsonObject = null;
        try (InputStream is = new URL(url).openStream(); 
             BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); ) {
            
            String jsonText = readAll(rd);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonText);
            jsonObject = (JSONObject) obj;
        } catch(Exception ex){
            ex.printStackTrace();
        } 
        
        return jsonObject;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
