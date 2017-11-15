package az.mm.arbitrage.db;

import az.mm.arbitrage.exceptionHandler.ExceptionHandler;
import az.mm.arbitrage.model.Bank;
import com.ibatis.common.jdbc.ScriptRunner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class DBConnection {
        
    private static final DBConnection instance = new DBConnection();

    private DBConnection() {}
    
    //Singleton pattern
    public static DBConnection getInstance() {
        return instance;
    }

    private Connection getDBConnection() {
        Connection connection = null;
        Properties props = new Properties();

        try( FileInputStream in = new FileInputStream("src/az/mm/arbitrage/resources/db.properties"); ) {
            props.load(in);
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            connection = DriverManager.getConnection(props.getProperty("db.url"));
        } catch (SQLException | IOException e) {
            ExceptionHandler.catchMessage(this, new Object(){}.getClass().getEnclosingMethod().getName(), e);
        } 

        return connection;
    }

    /**
     * Bazada movcud olan melumatlar http://www.animezenne.az/ saytina mexsusdur.
     * Asagidaki sorgu 'view' uzerinden cagirilir
     */
    public List<Bank> getAniMezenneBankList() {
        checkDatabaseExist();
        List<Bank> bankList = new ArrayList<>();

        String sql = "SELECT b.name, SUM(bUSD) bUSD, SUM(sUSD) sUSD, SUM(bEUR) bEUR, SUM(sEUR) sEUR, SUM(bRUB) bRUB, SUM(sRUB) sRUB, SUM(bGBP) bGBP, SUM(sGBP) sGBP, SUM(bTRY) bTRY, SUM(sTRY) sTRY, DATE \n" +
                    "FROM (\n" +
                    "SELECT r.b_id,  \n" +
                    "IF(r.c_id=1, r.buy, 0) AS bUSD, \n" +
                    "IF(r.c_id=1, r.sell, 0) AS sUSD, \n" +
                    "IF(r.c_id=3, r.buy, 0) AS bEUR, \n" +
                    "IF(r.c_id=3, r.sell, 0) AS sEUR, \n" +
                    "IF(r.c_id=4, r.buy, 0) AS bRUB, \n" +
                    "IF(r.c_id=4, r.sell, 0) AS sRUB, \n" +
                    "IF(r.c_id=2, r.buy, 0) AS bGBP, \n" +
                    "IF(r.c_id=2, r.sell, 0) AS sGBP, \n" +
                    "IF(r.c_id=6, r.buy, 0) AS bTRY, \n" +
                    "IF(r.c_id=6, r.sell, 0) AS sTRY, \n" +
                    "DATE(r.date) AS DATE\n" +
                    "FROM (\n" +
                    "SELECT r.b_id, r.c_id, r.buy, r.sell, r.date FROM rates r\n" +
                    "WHERE r.c_id IN (1,2,3,4,6,7) AND r.b_id != 27  \n" +
                    "GROUP BY r.b_id, r.c_id, DATE(r.date)\n" +
                    ") r\n" +
                    ") t, banks b where t.b_id=b.id \n" +
                    "GROUP BY DATE, b_id  ORDER BY DATE DESC;";
            
//        System.out.println("sql:\n"+sql);
        System.out.println("Loading data from database..");
        
        try (Connection connection = getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql); ) {
            
            try(ResultSet rs = preparedStatement.executeQuery();){
                while (rs.next()) 
                    bankList.add(new Bank(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getDouble(11), rs.getDate(12).toLocalDate()));
            }
        } catch (SQLException e) {
            ExceptionHandler.catchMessage(this, new Object(){}.getClass().getEnclosingMethod().getName(), e);
        }

        return bankList;
    }
    
    
    /**
     * Bazada texminen 1 illik melumat movcuddur: 2016.10.29 - 2017.09.17
     * Lakin localda baza movcud deyilse, baza ve cedvellerin yaradilmasi scripti
     * ishe dushur ve sadece bir ay - 2017-ci ilin avqust ayi uchun melumatlar 
     * elave edilir
     */
    private void checkDatabaseExist() {
        try (Connection connection = getDBConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement("Show databases like 'animezenne'");) {
            
            try(ResultSet rs = preparedStatement.executeQuery();){
                if (!rs.next())
                    try(BufferedReader br = new BufferedReader(new FileReader("src/az/mm/arbitrage/resources/create.sql"));){
                        ScriptRunner runner = new ScriptRunner(connection, false, false);
                        runner.runScript(br);
                    }
            }
            
        } catch (SQLException | IOException e) {
            ExceptionHandler.catchMessage(this, new Object(){}.getClass().getEnclosingMethod().getName(), e);
            
        }
    }
    
    
    public Map<String, LocalDate> getMaxMinDate(){
        Map<String, LocalDate> map = new HashMap();
        try (Connection connection = getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT date(min(r.date)) as min, date(max(r.date)) as max FROM rates r"); ) {
            
            try(ResultSet rs = preparedStatement.executeQuery();){
                while (rs.next()){
                    map.put("min", rs.getDate(1).toLocalDate());
                    map.put("max", rs.getDate(2).toLocalDate());
                } 
            }
        } catch (SQLException e) {
            ExceptionHandler.catchMessage(this, new Object(){}.getClass().getEnclosingMethod().getName(), e);
        }
        
        return map;
    }
    
}
