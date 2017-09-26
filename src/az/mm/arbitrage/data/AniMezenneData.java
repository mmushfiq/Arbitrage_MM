package az.mm.arbitrage.data;

import az.mm.arbitrage.db.DBConnection;
import az.mm.arbitrage.model.Bank;
import java.util.List;

/**
 *
 * @author MM
 */
public class AniMezenneData extends Data {
    
    @Override
    public List<Bank> getBankList(){
        return DBConnection.getInstance().getAniMezenneBankList();
    }
    
}
