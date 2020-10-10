package protocolTRAMAP;

import lib.BeanDBAcces.MysqlConnector;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class DataBaseTRAMAP extends MysqlConnector {
    public DataBaseTRAMAP(String username, String password, String database) {
        super(username, password, database);
    }

    public synchronized String getUserPassword(String userName)
    {
        //PreparedStatement instruct = _con.prepareStatement("SELECT * FROM ")
        return null;
    }

    public synchronized void input_lory(String reservation, String container)
    {
        //PreparedStatement ps = _con.prepareStatement();
    }

    /*
        Statement instruc = _con.createStatement();
        ResultSet rs = instruc.executeQuery("show tables;");
        while(rs.next())
        {
            System.out.println(rs.getString(1));
        }
     */


}
