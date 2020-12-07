//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.CHAMAP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.MysqlConnector;
import security.SecurityHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class TraitementCHAMAP implements Traitement
{
    /********************************/
    /*           Variables          */
    /********************************/
    private SecurityHelper _sc;
    private MysqlConnector bd_compta;
    private ConsoleServeur _cs;

    /********************************/
    /*         Constructeurs        */
    /********************************/

    public TraitementCHAMAP(SecurityHelper _sc, MysqlConnector bd_compta, ConsoleServeur _cs) {
        this.bd_compta = bd_compta;
        this._cs = _cs;
        this._sc = _sc;
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_sc(SecurityHelper _sc) {
        this._sc = _sc;
    }

    public void set_cs(ConsoleServeur _cs) {
        this._cs = _cs;
    }

    public void setBd_compta(MysqlConnector bd_compta) {
        this.bd_compta = bd_compta;
    }

    @Override
    public void setConsole(ConsoleServeur cs) {
        this._cs = cs;
    }
    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void AfficheTraitement(String message)
    {
        if (_cs != null)
        {
            _cs.Affiche(message);
        }
        else
        {
            System.err.println("-- Le serveur n'a pas de console dédiée pour ce message -- " + message);
        }
    }

    @Override
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException {
        if(Requete instanceof DonneeLoginTraf)
            return traiteLOGIN((DonneeLoginTraf) Requete, client);
        if(Requete instanceof DonneeMakeBill)
            return traiteMakeBill((DonneeMakeBill) Requete, client);

        return traite404();
    }

    private Reponse traiteLOGIN(DonneeLoginTraf chargeUtile, Client client)
    {
        String username = chargeUtile.get_nom();
        long temps = chargeUtile.get_temps();
        double alea = chargeUtile.get_aleatoire();
        byte[] msgD = chargeUtile.get_pwdDigest();

        if(client.is_loggedIn())
        {
            return new ReponseCHAMAP(ReponseCHAMAP.NOK, "Le client est deja connecte dans le serveur", null);
        }

        try
        {
            PreparedStatement ps = bd_compta.getPreparedStatement("SELECT userpassword FROM personnel WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);

            ResultSet rs = bd_compta.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");

                if(_sc.CompareDigests(msgD, bddpass.getBytes(), temps, alea))
                {
                    client.set_loggedIn(true);
                    return new ReponseCHAMAP(ReponseCHAMAP.OK, null, null);
                }
                else
                {
                    return new ReponseCHAMAP(ReponseCHAMAP.NOK, "Mot de passe ou nom d'utilisateur erroné", null);
                }
            }
        }
        catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException | IOException e)
        {
            e.printStackTrace();
        }

        return new ReponseCHAMAP(ReponseCHAMAP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteMakeBill(DonneeMakeBill chargeUtile, Client client)
    {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String societe = chargeUtile.get_societe();
        int idFacture;

        if(chargeUtile.get_containers() != null)
        {
            try {
                for (int i = 0; i < chargeUtile.get_containers().size(); i++) {
                    PreparedStatement ps = bd_compta.getPreparedStatement("SELECT * FROM facture WHERE upper(societe) = upper(?) AND MONTH(date_facture) = ?");
                    ps.setString(1, societe);
                    ps.setInt(2, month);
                    ResultSet rs = bd_compta.ExecuteQuery(ps);
                    if(rs.next())
                    {
                        idFacture = rs.getInt("id");
                        MakeBill(idFacture, chargeUtile.get_mouvements().get(i), chargeUtile.get_containers().get(i), chargeUtile.get_destination());
                    }
                    else
                    { //todo : faire tarif
                        PreparedStatement insertStatement = bd_compta.getPreparedStatement("INSERT INTO facture " +
                                "(societe, date_facture, tva) VALUES (?,SYSDATE(),21)");
                        insertStatement.setString(1, societe);

                        bd_compta.Execute(insertStatement);

                        rs = bd_compta.ExecuteQuery(ps);
                        if(rs.next())
                        {
                            idFacture = rs.getInt("id");
                            MakeBill(idFacture, chargeUtile.get_mouvements().get(i), chargeUtile.get_containers().get(i), chargeUtile.get_destination());
                        }
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return null;
    }

    private void MakeBill(int facture, int mouvement, String container, String destination) throws SQLException {

        //todo: aller chercher le prix du tarif

        PreparedStatement ps = bd_compta
                .getPreparedStatement("INSERT INTO items_facture (facture, mouvement, container, destination, prix_htva) " +
                "VALUES (?,?,?,?,?)");
        ps.setInt(1, facture);
        ps.setInt(2, mouvement);
        ps.setString(3, container);
        ps.setString(4, destination);
        ps.setFloat(5, 3f);

        bd_compta.Execute(ps);
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseCHAMAP(ReponseCHAMAP.REQUEST_NOT_FOUND, "404 request not found", null);
    }



}
