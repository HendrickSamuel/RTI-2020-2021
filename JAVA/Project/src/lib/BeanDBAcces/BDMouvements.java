//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 11/10/2020

package lib.BeanDBAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BDMouvements extends MysqlConnector
{
    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/
    /**
     * @param username
     * @param password
     * @param database
     ******************************/
    public BDMouvements(String username, String password, String database)
    {
        super(username, password, database);
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

    public synchronized boolean tryLogin(String username, String password)
    {
        try {
            PreparedStatement ps = _con.prepareStatement("SELECT userpassword FROM logins WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String bddpass = rs.getString("userpassword");
                if(password.compareTo(bddpass) == 0)
                    return true;
                else
                    return false;
            }
            return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public synchronized String getReservation(String numReservation, String idContainer)
    {
        try {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?) AND UPPER(numeroReservation) = UPPER(?);");
            ps.setString(1, idContainer);
            ps.setString(2, numReservation);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Trouvé: " + rs.getString("x") + " - " + rs.getString("y"));
                return rs.getString("x") + "#" + rs.getString("y");
            }
            else
                return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public synchronized String getInputWithoutRes(String immatriculation, String idContainer)
    {
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM parc WHERE etat = 0");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                rs.updateString("idContainer",idContainer);
                rs.updateRow();
                return rs.getString("x") + "#" + rs.getString("y");
            }
            else
                return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public synchronized List<String> getListOperations(Date dateDebut, Date dateFin, String nomSociete, String destination)
    {
        try {
            PreparedStatement ps;
            if(nomSociete != null)
            {
               ps = _con.prepareStatement("SELECT * FROM ");
            }
            else
            {
               ps = _con.prepareStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?) AND UPPER(numeroReservation) = UPPER(?);");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

}
