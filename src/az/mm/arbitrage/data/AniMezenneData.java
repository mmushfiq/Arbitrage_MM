package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.db.DBConnection;
import az.mm.arbitrage.model.Bank;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author MM
 */
public class AniMezenneData extends Data {
    
    private static Hashtable<String, List<Data>> aniMezenneMap = new Hashtable(); //bunu hele ki saxlayib umumi Data classina tetbiq etmek..
    private static List<Bank> bankList; 
    private Date randomDate;

    public AniMezenneData(int id) {
        randomDate = getRandomDate();
    }

    static {
        bankList = DBConnection.getInstance().getAniMezenneBankList();
    }
    
    @Override
    public List<Bank> getBankList(){
        List<Bank> aniMezenneBankList;
        if(bankList != null)
            aniMezenneBankList = bankList;
        else 
            aniMezenneBankList = DBConnection.getInstance().getAniMezenneBankList();
        
        aniMezenneBankList = aniMezenneBankList.stream()
                                .filter(b -> b.getDate().equals(randomDate))
                                .collect(Collectors.toList());
        return aniMezenneBankList;
    }
    
    @Override
    public int getDataId(){
        return randomDate.hashCode();
    }
    
    @Override
    public LocalDate getDate() {
        return randomDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    private Date getRandomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(2016, 10, 29).toEpochDay();
        int maxDay = (int) LocalDate.of(2017, 9, 17).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        return java.sql.Date.valueOf(randomDate);
    } 

}
