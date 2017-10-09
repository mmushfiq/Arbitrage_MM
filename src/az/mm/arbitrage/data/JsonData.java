package az.mm.arbitrage.data;

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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author USER
 */
public class JsonData extends Data {
    
    @Override
    public List<Bank> getBankList() {
        return new ArrayList<Bank>();
    }


    @Override
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
        try {
            for (int i = 0; i < currencies.length; i++) {
                JSONObject jsonObject = readJsonFromUrl("http://api.fixer.io/latest?base=" + currencies[i]);
                Map<String, Double> map = (Map) jsonObject.get("rates");
                ratesMap.put(currencies[i], map);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            return ratesMap;
        }
    }

    public static JSONObject readJsonFromUrl(String url) throws Exception {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonText);
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // http://api.fixer.io/latest?base=USD
        /*
         USD CAD CHF GBP JPY RUB TRY EUR
         {
         base: "USD",
         date: "2017-09-01",
         rates: {
         AUD: 1.2602,
         BGN: 1.6408,
         BRL: 3.1395,
         CAD: 1.2441,
         CHF: 0.95982,
         CNY: 6.5591,
         CZK: 21.877,
         DKK: 6.2398,
         GBP: 0.77244,
         HKD: 7.8248,
         HRK: 6.2261,
         HUF: 255.95,
         IDR: 13318,
         ILS: 3.5737,
         INR: 64.034,
         JPY: 110.14,
         KRW: 1120.3,
         MXN: 17.836,
         MYR: 4.2705,
         NOK: 7.7647,
         NZD: 1.3958,
         PHP: 51.091,
         PLN: 3.5576,
         RON: 3.856,
         RUB: 57.737,
         SEK: 7.9512,
         SGD: 1.3545,
         THB: 33.17,
         TRY: 3.438,
         ZAR: 12.935,
         EUR: 0.83893
         }
         }
         */
        try {

            JSONObject jsonObject = readJsonFromUrl("http://api.fixer.io/latest?base=USD");
            System.out.println(jsonObject);

            String name = (String) jsonObject.get("base");
            System.out.println(name);

//            System.out.println(jsonObject.get("rates"));
            // loop array
//            JSONArray msg = (JSONArray) jsonObject.get("rates");
            Map<String, Double> map = (Map) jsonObject.get("rates");

            for (String key : map.keySet()) {
                System.out.println(key + " --> " + map.get(key));
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
