package az.mm.arbitrage.db;

import az.mm.arbitrage.model.Bank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MM
 */
public class DBConnection {
    
    public static void main(String[] args) {
        for(Bank b: new DBConnection().getAniMezenneBankList()){
            System.out.println(b);
        }
    }
    
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
            return connection;
        } catch (SQLException e) {
            infoCatchMessage(e, "getDBConnection");
        }

        return connection;
    }

    private void close(PreparedStatement preparedStatement, Connection connection) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                infoCatchMessage(ex, "close");
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                infoCatchMessage(ex, "close");
            }
        }
    }

    public List<Bank> getAniMezenneBankList() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Bank> bankList = new ArrayList<>();
        String period = "";
        period = " and DATE_FORMAT(date,'%Y-%m-%d')='2017-05-22'";  //DATE_FORMAT(date,'%Y-%m-%d') between '2017-09-02' and '2017-09-07' - DATE_FORMAT(date,'%Y-%m')='2017-09'
        try {
            connection = getDBConnection();
            //sqli sonra deyishmek, view edib ordan chixarmaq..
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
                        "WHERE cr.currency_id IN (1,2,3,4,6,7) AND cr.bank_id != 27 AND cr.bank_id=b.id\n" + period +
                        "GROUP BY cr.bank_id, cr.currency_id, DATE(cr.date)\n" +
                        "ORDER BY cr.date DESC, cr.bank_id, cr.currency_id) cr) t\n" +
                        "GROUP BY DATE, bank_id\n" +
                        "ORDER BY DATE DESC;";
//            System.out.println(sql);
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                bankList.add(new Bank(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getDouble(11), rs.getDate(12)));
            }
        } catch (Exception e) {
            infoCatchMessage(e, "getBankList");
        } finally {
            close(preparedStatement, connection);
            return bankList;
        }
    }

    public static void infoCatchMessage(Exception e, String methodName) {
        String message = methodName + " method catch --> " + e;
        System.out.println(message);
        e.printStackTrace();
    }

}
