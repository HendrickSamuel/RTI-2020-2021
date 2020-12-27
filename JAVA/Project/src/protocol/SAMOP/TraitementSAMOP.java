//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;
import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.MysqlConnector;
import security.SecurityHelper;

import java.security.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TraitementSAMOP implements Traitement
{

    /********************************/
    /*           Variables          */
    /********************************/
    private SecurityHelper _securityHelper;
    private MysqlConnector _bd;
    private ConsoleServeur _cs;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementSAMOP()
    {

    }

    public TraitementSAMOP(SecurityHelper _sc, MysqlConnector bd_compta, ConsoleServeur _cs)
    {
        this._securityHelper = _sc;
        this._bd = bd_compta;
        this._cs = _cs;
    }


    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_sc(SecurityHelper _sc)
    {
        this._securityHelper = _sc;
    }

    public void setBd_compta(MysqlConnector bd_compta)
    {
        this._bd = bd_compta;
    }

    @Override
    public void setConsole(ConsoleServeur cs)
    {
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
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException
    {
        if(Requete instanceof DonneeLogin)
            return traiteLogin((DonneeLogin) Requete, client);
        if(Requete instanceof DonneeComputeSal)
            return traiteComputeSal((DonneeComputeSal) Requete, client);
        if(Requete instanceof DonneeValideSal)
            return traiteValideSal((DonneeValideSal) Requete, client);
        if(Requete instanceof DonneeLaunchPayement)
            return traiteLaunchPayement((DonneeLaunchPayement) Requete, client);
        if(Requete instanceof DonneeLaunchPayements)
            return traiteLaunchPayements((DonneeLaunchPayements) Requete, client);
        if(Requete instanceof DonneeAskPayements)
            return traiteAskPayements((DonneeAskPayements) Requete, client);

        return traite404();
    }

    private Reponse traiteLogin(DonneeLogin chargeUtile,  Client client)
    {
        System.out.println("traiteLogin");

        String username = chargeUtile.get_username();
        String password = chargeUtile.get_password();

        System.out.println("username : " + username);
        System.out.println("password : " + password);

        if(client.is_loggedIn())
        {
            return new ReponseSAMOP(ReponseSAMOP.NOK, "Le client est deja connecte dans le serveur", chargeUtile);
        }

        try
        {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT userpassword, fonction FROM personnel WHERE UPPER(username) = UPPER(?);");
            ps.setString(1, username);
            ResultSet rs = _bd.ExecuteQuery(ps);


            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");
                String fonction = rs.getString("fonction");

                if(fonction.equals("comptable") || fonction.equals("chef-comptable"))
                {
                    if(password.equals(bddpass))
                    {
                        client.set_loggedIn(true);

                        return new ReponseSAMOP(ReponseSAMOP.OK, null, null);
                    }
                    return new ReponseSAMOP(ReponseSAMOP.NOK, "Mot de passe ou nom d'utilisateur errone", null);
                }
                return new ReponseSAMOP(ReponseSAMOP.NOK, "Vous n'etes pas habilite pour cette action", null);
            }
            return new ReponseSAMOP(ReponseSAMOP.NOK, "Mot de passe ou nom d'utilisateur errone", null);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return new ReponseSAMOP(ReponseSAMOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteComputeSal(DonneeComputeSal chargeUtile,  Client client)
    {
        System.out.println("traiteComputeSal");

        String username = chargeUtile.get_username();
        byte[] signature = chargeUtile.get_signature();

        try
        {
            if(_securityHelper.verifySignature(username.getBytes(), signature, "ClientKeyEntry"))
            {
                PreparedStatement ps = _bd.getPreparedStatement("SELECT fonction FROM personnel WHERE UPPER(username) = UPPER(?);");
                ps.setString(1, username);
                ResultSet rs = _bd.ExecuteQuery(ps);

                if(rs!=null && rs.next())
                {
                    String fonction = rs.getString("fonction");

                    if(fonction.equals("chef-comptable"))
                    {
                            //todo calculer les salaires
                            return new ReponseSAMOP(ReponseSAMOP.OK, null, null);
                    }
                    return new ReponseSAMOP(ReponseSAMOP.NOK, "Vous n'etes pas habilite pour cette action", null);
                }
            }
            return new ReponseSAMOP(ReponseSAMOP.NOK, "Signature non correcte", null);
        }
        catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | SQLException e)
        {
            e.printStackTrace();
        }
        return new ReponseSAMOP(ReponseSAMOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteValideSal(DonneeValideSal chargeUtile,  Client client)
    {
        System.out.println("traiteValideSal");

        String username = chargeUtile.get_username();
        byte[] signature = chargeUtile.get_signature();

        try
        {
            if(_securityHelper.verifySignature(username.getBytes(), signature, "ClientKeyEntry"))
            {
                PreparedStatement ps = _bd.getPreparedStatement("SELECT fonction FROM personnel WHERE UPPER(username) = UPPER(?);");
                ps.setString(1, username);
                ResultSet rs = _bd.ExecuteQuery(ps);

                if(rs!=null && rs.next())
                {
                    String fonction = rs.getString("fonction");

                    if(fonction.equals("chef-comptable"))
                    {
                        //todo : retourner la liste
                        return new ReponseSAMOP(ReponseSAMOP.OK, null, null);
                    }
                    return new ReponseSAMOP(ReponseSAMOP.NOK, "Vous n'etes pas habilite pour cette action", null);
                }
            }
            return new ReponseSAMOP(ReponseSAMOP.NOK, "Signature non correcte", null);
        }
        catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | SQLException e)
        {
            e.printStackTrace();
        }
        return new ReponseSAMOP(ReponseSAMOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteLaunchPayement(DonneeLaunchPayement chargeUtile,  Client client)
    {
        System.out.println("traiteLaunchPayement");

        String empName = chargeUtile.getEmpName();



        return new ReponseSAMOP(ReponseSAMOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteLaunchPayements(DonneeLaunchPayements chargeUtile,  Client client)
    {
        System.out.println("traiteLaunchPayements");

        return new ReponseSAMOP(ReponseSAMOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteAskPayements(DonneeAskPayements chargeUtile,  Client client)
    {
        System.out.println("traiteAskPayements");

        int mois = chargeUtile.getMonth();

        return new ReponseSAMOP(ReponseSAMOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseSAMOP(ReponseSAMOP.REQUEST_NOT_FOUND, "404 request not found", null);
    }
}