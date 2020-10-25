//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 11/10/2020

package lib.BeanDBAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BDMouvements extends MysqlConnector
{
    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public BDMouvements(String username, String password, String database) throws SQLException, ClassNotFoundException
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

    public synchronized ResultSet getContainerById(String idContainer)
    {
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM containers WHERE UPPER(idContainer) = UPPER(?) ");
            ps.setString(1,idContainer);
            return ps.executeQuery();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    public synchronized boolean insertSociete(String nom, String email, String telephone, String adresse)
    {
        try
        {
            PreparedStatement ps = _con.
                    prepareStatement("INSERT into Societes (nom, email, telephone, adresse) VALUES (?,?,?,?);");
            ps.setString(1,nom);
            ps.setString(2,email);
            ps.setString(3,telephone);
            ps.setString(4,adresse);
            ps.execute();
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return false;
        }
    }

    public synchronized boolean insertContainer(String id, String societe, String contenu, Float capacite, String danger, Float poids)
    {
        try
        {
            PreparedStatement ps = _con.
                    prepareStatement("INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES (?,?,?,?,?,?);");
            ps.setString(1, id);
            ps.setString(2, societe);
            ps.setString(3, contenu);
            ps.setFloat(4, capacite);
            ps.setString(5, danger);
            ps.setFloat(6, poids);

            ps.execute();
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return false;
        }

    }

    public synchronized ResultSet getSocieteById(String idSociete)
    {
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM societes WHERE UPPER(nom) = UPPER(?) ");
            ps.setString(1,idSociete);
            return ps.executeQuery();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

}
