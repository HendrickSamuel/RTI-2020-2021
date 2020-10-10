/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package lib.BeanDBAcces;

import java.sql.*;
import java.util.TimeZone;

public class MysqlConnector
{
    /********************************/
    /*           Variables          */
    /********************************/
    protected Connection _con;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public MysqlConnector(String username, String password, String database)
    {
        try
        {
            Class driver = Class.forName("com.mysql.cj.jdbc.Driver");
            _con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database+"?serverTimezone="+ TimeZone.getDefault().getID(), username, password);
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }
}
