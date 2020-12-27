//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package Serveurs.Compta.ClientSecure;

import genericRequest.MyProperties;
import protocol.SAMOP.*;
import security.SecurityHelper;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SecureClientCompta
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String fichierKeystore = "./Confs/ClientKeyVault";
    private String mdpKeystore = "password";

    private SecurityHelper _securityHelper;
    private boolean _connected;
    private String _login;
    private String _password;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private SSLSocket cliSock;

    private String _serverIp;
    private int _port;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public SecureClientCompta() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
    {
        MyProperties mp = new MyProperties("./Confs/Serveur_Compta.conf");
        _port = Integer.parseInt(mp.getContent("PORT_SALARY"));
        _serverIp = mp.getContent("SERVER_SAMOP_IP");

        _securityHelper = new SecurityHelper();
        _securityHelper.initKeyStore("./Confs/ClientKeyVault","password");
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public boolean is_connected()
    {
        return _connected;
    }

    public String get_login()
    {
        return _login;
    }

    public String get_password()
    {
        return _password;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_connected(boolean _connected)
    {
        this._connected = _connected;
    }

    public void set_login(String _login)
    {
        this._login = _login;
    }

    public void set_password(String _password)
    {
        this._password = _password;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private boolean connectToServer()
    {
        ois=null;
        oos=null;
        cliSock = null;

        try
        {
            //Keystore
            KeyStore ServerKs = KeyStore.getInstance("JKS");
            ServerKs.load(new FileInputStream(fichierKeystore), mdpKeystore.toCharArray());

            //Contexte
            SSLContext SslC = SSLContext.getInstance("SSLv3");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ServerKs, mdpKeystore.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ServerKs);
            SslC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            //Factory
            SSLSocketFactory SslSFac = SslC.getSocketFactory();

            //Socket
            cliSock = (SSLSocket) SslSFac.createSocket(_serverIp, _port);

            System.out.println(cliSock.getInetAddress().toString());
            return true;
        }
        catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendReq(RequeteSAMOP req)
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

    private ReponseSAMOP readRep()
    {
        // Lecture de la réponse
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            return (ReponseSAMOP)ois.readObject();
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

    public ReponseSAMOP sendLogin()
    {
        if(cliSock == null)
        {
            connectToServer();
        }

        DonneeLogin dl = new DonneeLogin();

        dl.set_username(get_login());
        dl.set_password(get_password());

        RequeteSAMOP req = new RequeteSAMOP(dl);

        sendReq(req);

        ReponseSAMOP rep = readRep();

        return rep;
    }

    public ReponseSAMOP sendComputeSal() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, SignatureException, NoSuchProviderException, InvalidKeyException
    {
        DonneeComputeSal dcl = new DonneeComputeSal();

        dcl.set_username(get_login());
        dcl.set_signature(_securityHelper.signMessage(get_login().getBytes(), "ClientKeyVault", "password"));

        RequeteSAMOP req = new RequeteSAMOP(dcl);

        sendReq(req);

        ReponseSAMOP rep = readRep();

        return rep;
    }

    public ReponseSAMOP sendValidateSal() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, SignatureException, NoSuchProviderException, InvalidKeyException
    {
        DonneeValideSal dvl = new DonneeValideSal();

        dvl.set_username(get_login());
        dvl.set_signature(_securityHelper.signMessage(get_login().getBytes(), "ClientKeyVault", "password"));

        RequeteSAMOP req = new RequeteSAMOP(dvl);

        sendReq(req);

        ReponseSAMOP rep = readRep();

        return rep;
    }

    public ReponseSAMOP sendLaunchPayment(String empName)
    {
        DonneeLaunchPayement dlp = new DonneeLaunchPayement();

        dlp.setEmpName(empName);

        RequeteSAMOP req = new RequeteSAMOP(dlp);

        sendReq(req);

        ReponseSAMOP rep = readRep();

        return rep;
    }

    public ReponseSAMOP sendLaunchPayments()
    {
        DonneeLaunchPayement dlp = new DonneeLaunchPayement();

        RequeteSAMOP req = new RequeteSAMOP(dlp);

        sendReq(req);

        ReponseSAMOP rep = readRep();

        return rep;
    }

    public ReponseSAMOP sendAskPayments(int month)
    {
        DonneeAskPayements dap = new DonneeAskPayements();

        dap.setMonth(month);

        RequeteSAMOP req = new RequeteSAMOP(dap);

        sendReq(req);

        ReponseSAMOP rep = readRep();

        return rep;
    }
}