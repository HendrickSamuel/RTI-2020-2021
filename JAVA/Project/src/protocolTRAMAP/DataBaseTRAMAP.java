/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import lib.BeanDBAcces.MysqlConnector;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class DataBaseTRAMAP extends MysqlConnector
{
    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DataBaseTRAMAP(String username, String password, String database)
    {
        super(username, password, database);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public synchronized String getUserPassword(String userName)
    {
        //PreparedStatement instruct = _con.prepareStatement("SELECT * FROM ")
        return null;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
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
