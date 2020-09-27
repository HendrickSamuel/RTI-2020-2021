package lib.BeanDBAcces;

import java.sql.*;
import java.util.TimeZone;

public class MysqlConnector {
    private Connection _con;

    public MysqlConnector(String username, String password, String database)
    {
        try {
            Class driver = Class.forName("com.mysql.cj.jdbc.Driver");
            _con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_MOUVEMENTS?serverTimezone="+ TimeZone.getDefault().getID(), "newuser", "");
            Statement instruc = _con.createStatement();
            ResultSet rs = instruc.executeQuery("show tables;");
            while(rs.next())
            {
                System.out.println(rs.getString(1));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
