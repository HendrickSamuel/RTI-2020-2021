package lib.BeanDBAcces;

import java.sql.*;
import java.util.TimeZone;

public class MysqlConnector {
    protected Connection _con;

    public MysqlConnector(String username, String password, String database)
    {
        try {
            Class driver = Class.forName("com.mysql.cj.jdbc.Driver");
            _con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database+"?serverTimezone="+ TimeZone.getDefault().getID(), username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
