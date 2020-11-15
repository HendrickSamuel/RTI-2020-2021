//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.MysqlConnector;
import security.SecurityHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TraitementBISAMAP implements Traitement {

    /********************************/
    /*           Variables          */
    /********************************/
    private ConsoleServeur _cs;
    private MysqlConnector bd_compta;
    private SecurityHelper _securityHelper;


    /********************************/
    /*         Constructeurs        */
    /********************************/

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/
    @Override
    public void setConsole(ConsoleServeur cs) {
        _cs = cs;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException {
        if(Requete instanceof DonneeLogin)
            return traiteLOGIN((DonneeLogin)Requete, client);

        return traite404();
    }

    @Override
    public void AfficheTraitement(String message) {
        if (_cs != null)
        {
            _cs.Affiche(message);
        }
        else
        {
            System.err.println("-- Le serveur n'a pas de console dédiée pour ce message -- " + message);
        }
    }

    private Reponse traiteLOGIN(DonneeLogin chargeUtile, Client client)
    {
        String username = chargeUtile.get_nom();
        long temps = chargeUtile.get_temps();
        double alea = chargeUtile.get_aleatoire();
        byte[] msgD = chargeUtile.get_pwdDigest();

        if(client.is_loggedIn())
        {
            return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Le client est deja connecte dans le serveur", null);
        }
        try
        {
            PreparedStatement ps = bd_compta.getPreparedStatement("SELECT userpassword FROM personnel WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);

            ResultSet rs = bd_compta.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");

                if(_securityHelper.CompareDigests(msgD, bddpass.getBytes(), temps, alea))
                {
                    client.set_loggedIn(true);

                    client.set_sessionKey(_securityHelper.getSecretKey());
                    client.set_hmacKey(_securityHelper.getSecretKey());

                    chargeUtile.set_sessionKey(_securityHelper.cipherSecretKey(client.get_sessionKey(), "ClientKeyEntry")); //todo: alias ne devrait pas etre en dur ?
                    chargeUtile.set_hmac(_securityHelper.cipherSecretKey(client.get_hmacKey(), "ClientKeyEntry"));

                    return new ReponseBISAMAP(ReponseBISAMAP.OK, null, chargeUtile);
                }
                else
                {
                    return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Mot de passe ou nom d'utilisateur erroné", null);
                }
            }
        }
        catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException | IOException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | KeyStoreException e)
        {
            e.printStackTrace();
        }

        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteNextBill(DonneeGetNextBill chargeUtile, Client client)
    {
        try {
            PreparedStatement ps = bd_compta.getPreparedStatement("SELECT * FROM facture WHERE facture_validee = false ORDER BY id;");
            ResultSet rs = bd_compta.ExecuteQuery(ps);

            if(rs.next())
            {
                Facture facture = new Facture();

                chargeUtile.setFactureCryptee(_securityHelper.cipherObject(facture, client.get_sessionKey()));
            }
            else
            {

            }
        } catch (SQLException | IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseBISAMAP(ReponseBISAMAP.REQUEST_NOT_FOUND, "404 request not found", null);
    }
}
