package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.db.DBConnection;
import az.mm.arbitrage.model.Bank;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class AniMezenneData extends Data {
    
    private static final List<Bank> bankList; 
    private Date randomDate;

    public AniMezenneData(int id) {
        randomDate = getRandomDate();
    }

    static {
        bankList = DBConnection.getInstance().getAniMezenneBankList();
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
//        return randomDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return new java.sql.Date(randomDate.getTime()).toLocalDate();
    }
    
    private Date getRandomDate() {
        int minDay = (int) LocalDate.of(2016, 10, 29).toEpochDay();
        int maxDay = (int) LocalDate.of(2017, 9, 17).toEpochDay();
        long randomDay = minDay + new Random().nextInt(maxDay - minDay);
        LocalDate localDate = LocalDate.ofEpochDay(randomDay);

        return java.sql.Date.valueOf(localDate);
    } 

}
