package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.db.DBConnection;
import az.mm.arbitrage.model.Bank;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class AniMezenneData extends Data {
    
    private static final List<Bank> bankList; 
    private static Map<String, LocalDate> maxMinDate;
    private LocalDate randomDate;
    
    static {
        bankList = DBConnection.getInstance().getAniMezenneBankList();
        maxMinDate = DBConnection.getInstance().getMaxMinDate();
    }

    public AniMezenneData(int id) {
        randomDate = getRandomDate();
    }

    @Override
    public List<Bank> getBankList(){
        List<Bank> oneDayList = bankList.stream()
                                .filter(b -> b.getDate().equals(randomDate))
                                .collect(Collectors.toList());
        return oneDayList;
    }
    
    @Override
    public int getDataId(){
        return randomDate.hashCode();
    }
    
    @Override
    public LocalDate getDate() {
        return randomDate;
    }
    
    private LocalDate getRandomDate() {
        int minDay = (int) maxMinDate.get("min").toEpochDay();
        int maxDay = (int) maxMinDate.get("max").toEpochDay();
        long randomDay = minDay + new Random().nextInt(maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay);
    } 

}
