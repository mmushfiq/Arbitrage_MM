package az.mm.arbitrage.db;

import az.mm.arbitrage.model.Bank;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author MM
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

        //url, login, password-u sonra kodun icherisinden chixarmaq..
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/animezenne?zeroDateTimeBehavior=convertToNull", "root", "root");
        } catch (SQLException e) {
            infoCatchMessage(e, "getDBConnection");
        }

        return connection;
    }


    public List<Bank> getAniMezenneBankList() {
        List<Bank> bankList = new ArrayList<>();

            //sqli sonra deyishmek, view yaradib ordan chixarmaq..
            String sql = "SELECT name, \n" +
                        "IF(SUM(bUSD)=0, -1, SUM(bUSD)) bUSD, \n" +
                        "IF(SUM(sUSD)=0, -1, SUM(sUSD)) sUSD, \n" +
                        "IF(SUM(bEUR)=0, -1, SUM(bEUR)) bEUR, \n" +
                        "IF(SUM(sEUR)=0, -1, SUM(sEUR)) sEUR, \n" +
                        "IF(SUM(bRUB)=0, -1, SUM(bRUB)) bRUB, \n" +
                        "IF(SUM(sRUB)=0, -1, SUM(sRUB)) sRUB, \n" +
                        "IF(SUM(bGBP)=0, -1, SUM(bGBP)) bGBP, \n" +
                        "IF(SUM(sGBP)=0, -1, SUM(sGBP)) sGBP, \n" +
                        "IF(SUM(bTRY)=0, -1, SUM(bTRY)) bTRY, \n" +
                        "IF(SUM(sTRY)=0, -1, SUM(sTRY)) sTRY, \n" +
                        "DATE\n" +
                        "FROM\n" +
                        "(\n" +
                        "SELECT cr.bank_id, cr.name, \n" +
                        "IF(cr.currency_id=1, cr.buy, 0) AS bUSD, \n" +
                        "IF(cr.currency_id=1, cr.sell, 0) AS sUSD, \n" +
                        "IF(cr.currency_id=3, cr.buy, 0) AS bEUR, \n" +
                        "IF(cr.currency_id=3, cr.sell, 0) AS sEUR, \n" +
                        "IF(cr.currency_id=4, cr.buy, 0) AS bRUB, \n" +
                        "IF(cr.currency_id=4, cr.sell, 0) AS sRUB, \n" +
                        "IF(cr.currency_id=2, cr.buy, 0) AS bGBP, \n" +
                        "IF(cr.currency_id=2, cr.sell, 0) AS sGBP, \n" +
                        "IF(cr.currency_id=6, cr.buy, 0) AS bTRY, \n" +
                        "IF(cr.currency_id=6, cr.sell, 0) AS sTRY, \n" +
                        "DATE(cr.date) AS DATE\n" +
                        "FROM\n" +
                        "(\n" +
                        "SELECT cr.bank_id, b.name, cr.currency_id, cr.buy, cr.sell, cr.date\n" +
                        "FROM currency_rate cr, bank b\n" +
                        "WHERE cr.currency_id IN (1,2,3,4,6,7) AND cr.bank_id != 27 AND cr.bank_id=b.id and DATE_FORMAT(date,'%Y-%m-%d')=? \n"+
                        "GROUP BY cr.bank_id, cr.currency_id, DATE(cr.date)\n" +
                        "ORDER BY cr.date DESC, cr.bank_id, cr.currency_id) cr) t\n" +
                        "GROUP BY DATE, bank_id\n" +
                        "ORDER BY DATE DESC;";
            
        System.out.println("sql:\n"+sql);
        
        try (Connection connection = getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql); ) {
            
            preparedStatement.setDate(1, getRandomDate());
            try(ResultSet rs = preparedStatement.executeQuery();){
                while (rs.next()) 
                    bankList.add(new Bank(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getDouble(11), rs.getDate(12)));
            }
            
        } catch (Exception e) {
            infoCatchMessage(e, "getBankList");
        }

        return bankList;
    }
    
    private java.sql.Date getRandomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(2016, 10, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2017, 9, 17).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        
        return java.sql.Date.valueOf(randomDate.toString());
    }

    public void infoCatchMessage(Exception e, String methodName) {
        String message = methodName + " method catch --> " + e;
        System.out.println(message);
        e.printStackTrace();
    }

}
