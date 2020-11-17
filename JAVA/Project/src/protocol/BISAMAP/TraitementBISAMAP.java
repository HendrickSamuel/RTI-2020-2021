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
import javax.crypto.SealedObject;
import java.io.IOException;
import java.security.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    public TraitementBISAMAP(ConsoleServeur _cs, MysqlConnector bd_compta, SecurityHelper _securityHelper) {
        this._cs = _cs;
        this.bd_compta = bd_compta;
        this._securityHelper = _securityHelper;
    }

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
        if(Requete instanceof DonneeGetNextBill)
            return traiteNextBill((DonneeGetNextBill)Requete, client);
        if(Requete instanceof DonneeValidateBill)
            return traiteValidateBill((DonneeValidateBill)Requete, client);
        if(Requete instanceof DonneeListBills)
            return traiteListBills((DonneeListBills)Requete, client);
        if(Requete instanceof DonneeSendBills)
            return traiteSendBills((DonneeSendBills)Requete, client);
        if(Requete instanceof DonneeRecPay)
            return traiteRecPay((DonneeRecPay)Requete, client);
        if(Requete instanceof DonneeListWaiting)
            return traiteListWaiting((DonneeListWaiting)Requete, client);

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
               Facture facture = this.factureFromResultSet(rs);

                chargeUtile.setFactureCryptee(_securityHelper.cipherObject(facture, client.get_sessionKey()));
                return new ReponseBISAMAP(ReponseBISAMAP.OK, null , chargeUtile);
            }
            else
            {
                return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Aucune facture n'est disponible", null);
            }
        } catch (SQLException | IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Une erreur s'est produite", null);
    }

    private Reponse traiteValidateBill(DonneeValidateBill chargeUtile, Client client)
    {
        try {
            if(_securityHelper.verifySignature(chargeUtile.get_content().getBytes(), chargeUtile.get_signature(), "ClientKeyEntry")) //todo: pas en dur
            {
                PreparedStatement ps = bd_compta.getPreparedStatement("SELECT * FROM facture WHERE id = ? AND facture_validee = false");
                ps.setInt(1, chargeUtile.get_factureNumber());
                ResultSet rs = bd_compta.ExecuteQuery(ps);
                if(rs.next())
                {
                    rs.updateBoolean("facture_validee",true);
                    rs.updateString("comptable_validateur", chargeUtile.get_comtpable());
                    bd_compta.UpdateResult(rs);
                    return new ReponseBISAMAP(ReponseBISAMAP.OK, null, chargeUtile);
                }
                else
                {
                    return new ReponseBISAMAP(ReponseBISAMAP.NOK, "La facture ne correspond pas", null);
                }

            }
            else
            {
                return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Signature non valide", null);
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | SQLException e) {
            e.printStackTrace();
        }
        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Erreur lors de la requet", null);
    }

    private Reponse traiteListBills(DonneeListBills chargeUtile, Client client)
    {
        try{
            if(_securityHelper.verifySignature(chargeUtile.get_content().getBytes(),
                    chargeUtile.get_signature(), "ClientKeyEntry"))
            {
                PreparedStatement ps = bd_compta.getPreparedStatement("SELECT * FROM facture WHERE date_facture BETWEEN ? AND ? AND facture_payee = false AND facture_validee = false;");
                ps.setDate(1, new java.sql.Date(chargeUtile.get_dateDepart().getTime()));
                ps.setDate(2, new java.sql.Date(chargeUtile.get_dateFin().getTime()));
                ResultSet rs = bd_compta.ExecuteQuery(ps);
                ArrayList<SealedObject> factures = new ArrayList<>();
                while(rs.next())
                {
                    Facture facture = factureFromResultSet(rs);

                    factures.add(_securityHelper.cipherObject(facture, client.get_sessionKey()));
                }
                chargeUtile.set_factures(factures);
                return new ReponseBISAMAP(ReponseBISAMAP.OK, null, chargeUtile);
            }
            else
            {
                return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Signature non valide", null);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException | KeyStoreException | SQLException | NoSuchPaddingException | IOException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Erreur lors de la requet", null);
    }

    private Reponse traiteSendBills(DonneeSendBills chargeUtile, Client client)
    {
        List<Integer> facts = chargeUtile.get_facturesToIgnore();
        try{
            if(_securityHelper.verifySignature(chargeUtile.get_content().getBytes(), chargeUtile.get_signature(), "ClientKeyEntry"))
            {
                PreparedStatement ps = bd_compta.getPreparedStatement("SELECT * FROM facture WHERE facture_validee");
                ResultSet rs = bd_compta.ExecuteQuery(ps);
                while (rs.next())
                {
                    if(facts == null || (facts!= null && !facts.contains(rs.getInt("id"))))
                    {
                        rs.updateBoolean("facture_envoyee", true);
                        bd_compta.UpdateResult(rs);
                    }
                    else
                    {
                        if(facts != null)
                            facts.remove(rs.getInt("id"));
                    }
                }
                chargeUtile.set_facturesToIgnore(facts); //renvoi des factures non traitées
                return new ReponseBISAMAP(ReponseBISAMAP.OK, null, chargeUtile);
            }
            else
            {
                return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Signature non valide", null);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException | KeyStoreException | SQLException e) {
            e.printStackTrace();
        }
        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Erreur lors de la requet", null);
    }

    private Reponse traiteRecPay(DonneeRecPay chargeUtile, Client client)
    {
        try{
            if(_securityHelper.verifyHMAC(chargeUtile.get_content().getBytes(), chargeUtile.get_hmac(), client.get_hmacKey()))
            {
                String infos = new String(_securityHelper.decipherMessage(chargeUtile.get_infosBancaireCryptees(), client.get_sessionKey()));
                PreparedStatement ps = bd_compta.getPreparedStatement("SELECT * FROM facture WHERE id = ?;");
                ps.setInt(1, chargeUtile.get_facture());
                ResultSet rs = bd_compta.ExecuteQuery(ps);
                if(rs.next())
                {
                    PreparedStatement psMontant =
                            bd_compta.getPreparedStatement(
                    "SELECT TRUNCATE(TRUNCATE(SUM(prix_htva),2)+TRUNCATE(SUM(prix_htva),2)*(f.tva/100), 2) as montant " +
                            "FROM items_facture " +
                            "INNER JOIN facture f on items_facture.facture = f.id " +
                            "WHERE facture = 1 " +
                            "GROUP BY facture");
                    ResultSet resMontant = bd_compta.ExecuteQuery(psMontant);
                    if(resMontant.next())
                    {
                        float montantAPayer = resMontant.getFloat("montant");
                        if(chargeUtile.get_montant() > montantAPayer-0.1 && chargeUtile.get_montant() < montantAPayer+0.1)
                        {
                            rs.updateString("moyen_payement",infos);
                            rs.updateBoolean("facture_payee",true);
                            bd_compta.UpdateResult(rs);
                            return new ReponseBISAMAP(ReponseBISAMAP.OK, null, chargeUtile);
                        }
                        else
                            return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Mauvais montant à payer - " + montantAPayer, null);
                    }
                    else
                        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Aucun montant à payer", null);
                }
                else
                    return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Facture non existante ou déjà payée", null);
            }
            else
                return new ReponseBISAMAP(ReponseBISAMAP.NOK, "HMAC non valide", null);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | SQLException e) {
            e.printStackTrace();
            return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Erreur lors de la requete: " + e.getMessage(), null);
        }
    }

    private Reponse traiteListWaiting(DonneeListWaiting chargeUtile, Client client)
    {
        try{
            if(_securityHelper.verifySignature(chargeUtile.get_content().getBytes(), chargeUtile.get_signature(), "ClientKeyVault"))
            {
                PreparedStatement ps;
                if(chargeUtile.get_nature() == DonneeListWaiting.Duree)
                {
                    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    String query = "SELECT * FROM facture WHERE facture_envoi = true AND facture_payee = false AND mois < ? AND annee <= ?;";
                    ps = bd_compta.getPreparedStatement(query);
                    ps.setInt(1, month);
                    ps.setInt(2, year);
                }
                else
                {
                    String query = "SELECT * FROM facture WHERE facture_envoi = true AND facture_payee = false AND upper(societe) = upper(?);";
                    ps = bd_compta.getPreparedStatement(query);
                    ps.setString(1, chargeUtile.get_societe());
                }

                ResultSet rs = bd_compta.ExecuteQuery(ps);
                ArrayList<SealedObject> factures = new ArrayList<SealedObject>();
                while(rs.next())
                {
                    Facture facture = factureFromResultSet(rs);
                    factures.add(_securityHelper.cipherObject(facture, client.get_sessionKey()));
                }
                chargeUtile.set_factures(factures);
                return new ReponseBISAMAP(ReponseBISAMAP.OK, null, chargeUtile);
            }
            else
            {
                return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Signature non valide", null);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException | KeyStoreException | SQLException | NoSuchPaddingException | IOException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Erreur lors de la requet", null);
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseBISAMAP(ReponseBISAMAP.REQUEST_NOT_FOUND, "404 request not found", null);
    }

    private Facture factureFromResultSet(ResultSet rs) throws SQLException {
        Facture facture = new Facture();
        facture.set_societe(rs.getString("societe"));
        facture.set_id(rs.getInt("id"));
        facture.set_annee(rs.getDate("date_facture").getYear());
        facture.set_mois(rs.getDate("date_facture").getMonth()+1);
        facture.set_tva(rs.getFloat("tva"));

        return facture;
    }
}
