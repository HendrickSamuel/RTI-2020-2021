//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 16/11/2020

package Serveurs.Compta.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import genericRequest.Reponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocol.BISAMAP.*;
import protocol.PIDEP.ReponsePIDEP;
import protocol.PIDEP.RequetePIDEP;
import security.SecurityHelper;

import javax.crypto.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientCompta {

    /********************************/
    /*           Variables          */
    /********************************/
    private SecurityHelper _securityHelper;
    private boolean _connected;
    private String _login;
    private String _pasword;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;

    private String _serverIp;
    private int _port;

    private SecretKey _sessionKey;
    private SecretKey _hmacKey;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientCompta() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        MyProperties mp = new MyProperties("./Confs/Serveur_Compta.conf");
        _port = Integer.parseInt(mp.getContent("PORT_BISAMAP"));
        _serverIp = mp.getContent("SERVER_BISAMAP_IP");

        _securityHelper = new SecurityHelper();
        _securityHelper.initKeyStore("./Confs/ClientKeyVault","password"); //todo: fichier config
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public boolean is_connected() {
        return _connected;
    }

    public String get_login() {
        return _login;
    }

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

    private boolean connectToServer()
    {
        ois=null; oos=null; cliSock = null;
        try {
            cliSock = new Socket(_serverIp, _port);
            System.out.println(cliSock.getInetAddress().toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendReq(RequeteBISAMAP req)
    {
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            return true;
        }
        catch (IOException e)
        {
            System.err.println("---sendReq Erreur réseau ? [" + e.getMessage() + "]");
            return false;
        }
    }

    private ReponseBISAMAP readRep()
    {
        // Lecture de la réponse
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            return (ReponseBISAMAP)ois.readObject();
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("---readRep erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("---readRep erreur IO = " + e.getMessage());
        }
        return null;
    }

    public boolean closeSocket()
    {
        try
        {
            cliSock.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("---closeSocket erreur IO = " + e.getMessage());
            return false;
        }
    }

    private void sendLogin() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, NoSuchPaddingException, UnrecoverableKeyException, IllegalBlockSizeException, BadPaddingException, KeyStoreException, InvalidKeyException {
        SendLogin(_login, _pasword);
    }

    public boolean SendLogin(String username, String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, NoSuchPaddingException, UnrecoverableKeyException, IllegalBlockSizeException, BadPaddingException, KeyStoreException, InvalidKeyException {
        _login = username;
        _pasword = password;

        connectToServer();
        DonneeLogin dl = new DonneeLogin();

        long temps = (new Date()).getTime();
        double alea = Math.random();

        dl.set_temps(temps);
        dl.set_aleatoire(alea);
        dl.set_pwdDigest(_securityHelper.createSaltedDigest(password, temps, alea));
        dl.set_nom(username);

        RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(dl);

        sendReq(requeteBISAMAP);

        ReponseBISAMAP reponseBISAMAP = readRep();
        if(reponseBISAMAP.getCode() == ReponseBISAMAP.OK)
        {
            dl = (DonneeLogin)reponseBISAMAP.getChargeUtile();
            _sessionKey = _securityHelper.decipherSecretKey(dl.get_sessionKey(), "ClientKeyVault","password"); //todo: charger du fichier
            _hmacKey = _securityHelper.decipherSecretKey(dl.get_hmac(), "ClientKeyVault","password"); //todo: charger du fichier
            _connected = true;
        }
        else
        {
            closeSocket();
            _connected = false;
        }

        return _connected;
    }

    public ReponseBISAMAP GetNextBill() throws Exception {
        if(_connected)
        {
            RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(new DonneeGetNextBill());
            sendReq(requeteBISAMAP);

            ReponseBISAMAP reponseBISAMAP = readRep();
            if(reponseBISAMAP.getCode() == ReponseBISAMAP.OK)
            {
                DonneeGetNextBill dgnb = (DonneeGetNextBill)reponseBISAMAP.getChargeUtile();
                dgnb.set_facture((Facture)_securityHelper.decipherObject(dgnb.getFactureCryptee(), _sessionKey));
                return reponseBISAMAP;
            }
            else
            {
                return reponseBISAMAP;
            }
        }
        else
        {
            throw new Exception("You must be connected");
        }
    }

    public ReponseBISAMAP ValidateBill(int idFactures) throws Exception {
        if(_connected)
        {
            DonneeValidateBill donneeValidateBill = new DonneeValidateBill(idFactures, _login);
            donneeValidateBill.set_signature(
                    _securityHelper.signMessage(donneeValidateBill.get_content().getBytes(), "ClientKeyVault", "password")); //todo: fichier ?

            RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(donneeValidateBill);

            sendReq(requeteBISAMAP);

            ReponseBISAMAP reponseBISAMAP = readRep();
            return reponseBISAMAP;
        }
        else
        {
            throw new Exception("You must be connected");
        }
    }

    public ReponseBISAMAP ListBills(String societe, Date dateDepart, Date dateFin) throws Exception {
        if(_connected)
        {
            DonneeListBills dlb = new DonneeListBills(societe, dateDepart, dateFin);
            dlb.set_signature(_securityHelper.signMessage(dlb.get_content().getBytes(), "ClientKeyVault","password"));

            RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(dlb);
            sendReq(requeteBISAMAP);

            ReponseBISAMAP reponseBISAMAP = readRep();
            if(reponseBISAMAP.getCode() == ReponseBISAMAP.OK)
            {
                dlb = (DonneeListBills) reponseBISAMAP.getChargeUtile();
                ArrayList<Facture> factures = new ArrayList<>();
                for(SealedObject factureCryptee : dlb.get_factures())
                {
                    factures.add((Facture) _securityHelper.decipherObject(factureCryptee, _sessionKey));
                }
                dlb.setFacturesDecryptees(factures);
            }
            return reponseBISAMAP;
        }
        else
        {
            throw new Exception("You must be connected");
        }
    }

    public ReponseBISAMAP SendBills(String comptable, List<Integer> factures) throws Exception {
        if(_connected)
        {
            DonneeSendBills dsb = new DonneeSendBills();
            dsb.set_facturesToIgnore(factures);
            dsb.set_userName(comptable);
            dsb.set_signature(_securityHelper.signMessage(dsb.get_content().getBytes(), "ClientKeyVault","password"));

            RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(dsb);
            sendReq(requeteBISAMAP);

            ReponseBISAMAP reponseBISAMAP = readRep();
            return reponseBISAMAP;
        }
        else
        {
            throw new Exception("You must be connected");
        }
    }

    public ReponseBISAMAP RecPay(int facture, float montant, String comptable, String carteBanquaire) throws Exception {
        if(_connected)
        {
            DonneeRecPay dsb = new DonneeRecPay(facture, montant, comptable);
            dsb.set_infosBancaireCryptees(_securityHelper.cipherMessage(carteBanquaire.getBytes(), _sessionKey));
            dsb.set_hmac(_securityHelper.createHMAC(dsb.get_content().getBytes(), _hmacKey));
            RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(dsb);
            sendReq(requeteBISAMAP);

            ReponseBISAMAP reponseBISAMAP = readRep();
            return reponseBISAMAP;
        }
        else
        {
            throw new Exception("You must be connected");
        }
    }

    public ReponseBISAMAP ListWaiting(String comptable, String societe) throws Exception {
        return ListWaiting(comptable, DonneeListWaiting.Societe, societe);
    }

    public ReponseBISAMAP ListWaiting(String comptable) throws Exception {
        return ListWaiting(comptable, DonneeListWaiting.Duree, null);
    }

    private ReponseBISAMAP ListWaiting(String comptable, int nature, String societe) throws Exception {
        if(_connected)
        {
            DonneeListWaiting dlw = new DonneeListWaiting(comptable, nature);
            dlw.set_societe(societe);
            dlw.set_signature(_securityHelper.signMessage(dlw.get_content().getBytes(), "ClientKeyVault","password"));
            RequeteBISAMAP requeteBISAMAP = new RequeteBISAMAP(dlw);
            sendReq(requeteBISAMAP);

            ReponseBISAMAP reponseBISAMAP = readRep();
            if(reponseBISAMAP.getCode() == ReponseBISAMAP.OK)
            {
                dlw = (DonneeListWaiting) reponseBISAMAP.getChargeUtile();
                ArrayList<Facture> factures = new ArrayList<>();
                for(SealedObject sealed : dlw.get_factures())
                {
                    factures.add((Facture) _securityHelper.decipherObject(sealed, _sessionKey));
                }
                dlw.set_facturesDecryptees(factures);
            }
            return reponseBISAMAP;
        }
        else
        {
            throw new Exception("You must be connected");
        }
    }

}
