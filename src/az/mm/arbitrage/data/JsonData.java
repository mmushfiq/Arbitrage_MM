package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.model.OptimalRate;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.*;
import javax.net.ssl.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class JsonData extends Data {
    private int dataId, i;
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
        return new ArrayList<>();
    }
    
    @Override
    public LocalDate getDate() {
        return date;
    }
    
    @Override
    public String[] getCurrencies() {
        return currencies.clone();
    }

    
    @Override
    public OptimalRate[][] getOptimalRatesAdjencyMatrix(Data data, String[] cur) {
        currencies = cur; // permutation alqoritminde sechim deyishe biler, ona gore de bu mutleq olmalidir
        return getOptimalRatesAdjencyMatrix();
    }
    
    private OptimalRate[][] getOptimalRatesAdjencyMatrix(){
        OptimalRate[][] R = new OptimalRate[currencies.length][currencies.length];

        getRatesMap().forEach((key, value) -> {
            value.forEach((currency, rate) -> {
                int index = Arrays.asList(currencies).indexOf(currency);
                R[i][index] = new OptimalRate("", rate);
            });
            R[i][i] = new OptimalRate("", 1.);
            i++;
        });

        printArr(this, R, currencies);
        return R;
    }


    private Map<String, Map<String, Double>> getRatesMap() {
        Map<String, Map<String, Double>> ratesMap = new LinkedHashMap();
        JSONObject jsonObject = null;
        
        String symbols = String.join(",", currencies);
        StringBuilder sb;
        for (int i = 0; i < currencies.length; i++) {
            sb = new StringBuilder("https://api.fixer.io/latest?base=");
            sb.append(currencies[i]).append("&symbols=").append(symbols);
            jsonObject = readJsonFromUrl(sb.toString());
            Map<String, Double> map = (Map) jsonObject.get("rates");
            ratesMap.put(currencies[i], map);
        }

        if(jsonObject != null) date = LocalDate.parse(jsonObject.get("date").toString());
        
        return ratesMap;
    }
   

    private JSONObject readJsonFromUrl(String url) {
        preventSSLHandshakeException();
        JSONObject jsonObject = null;
        try (InputStream is = new URL(url).openStream(); 
             BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); ) {
            
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) 
                sb.append((char) cp);
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(sb.toString());
            jsonObject = (JSONObject) obj;
        } catch(Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
        } 
        
        return jsonObject;
    }
    
    
    /**
     * Bu metodu muveqqeti yazmisham, yoxsa Exception verir. Qoshuldugum domen
     * bu kodu yazdigim muddetde SSL sertifikatina qoshuldu ve evvel http ile 
     * normal qoshuldugum halda sonradan exception verdi. https ile qoshulduqda
     * ise bele basha dushdum sertifikati JVM-e import etmelisen..
     */
    private static void preventSSLHandshakeException() {
        SSLContext ctx = null;
        TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() { return null; }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        }};
        
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        SSLContext.setDefault(ctx);
    }

}
