//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 11/10/2020

package lib.BeanDBAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

    public synchronized ResultSet getLogin(String username, String password)
    {
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT userpassword FROM logins WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);
            return ps.executeQuery();

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getReservation(String numReservation, String idContainer)
    {
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?) AND UPPER(numeroReservation) = UPPER(?);");
            ps.setString(1, idContainer);
            ps.setString(2, numReservation);
            return ps.executeQuery();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

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

    public synchronized ResultSet getInputWithoutRes(String immatriculation, String idContainer)
    {
        try
        {
            Statement ps = _con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return ps.executeQuery("SELECT * FROM parc WHERE etat = 0");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getListOperationsSociete(Date dateDebut, Date dateFin, String nomSociete)
    {
        ArrayList<String> resultats = new ArrayList<>();
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM mouvements" +
                    "INNER JOIN containers c on mouvements.idContainer = c.idContainer" +
                    "INNER JOIN societes s on c.idSociete = s.idSociete" +
                    "WHERE UPPER(s.nom) = UPPER(?)" +
                    "AND dateDepart >= ? AND (dateArrivee <= ? OR dateArrivee IS NULL);");
            ps.setString(1, nomSociete);
            ps.setDate(2, java.sql.Date.valueOf(dateDebut.toString()));
            ps.setDate(3, java.sql.Date.valueOf(dateFin.toString()));

            return ps.executeQuery();

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getListOperationsDestination(Date dateDebut, Date dateFin, String destination)
    {
        try
        {
            PreparedStatement ps = _con.prepareStatement("SELECT * FROM mouvements" +
                    "WHERE UPPER(destination) = UPPER(?)" +
                    "AND dateDepart >= ? AND (dateArrivee <= ? OR dateArrivee IS NULL);");
            ps.setString(1, destination);
            ps.setDate(2, java.sql.Date.valueOf(dateDebut.toString()));
            ps.setDate(3, java.sql.Date.valueOf(dateFin.toString()));

            return ps.executeQuery();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }
}
