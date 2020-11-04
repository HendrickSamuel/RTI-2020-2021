//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;

import java.io.*;
import java.util.Date;
import java.net.Socket;
import protocol.PIDEP.*;
import java.security.Security;
import genericRequest.MyProperties;
import java.security.MessageDigest;
import genericRequest.DonneeRequete;
import java.net.UnknownHostException;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ClientAnalysis
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String _codeProvider;
    private String _hash;
    private boolean _connect;
    private String _login;
    private String _pwd;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientAnalysis()
    {
        Security.addProvider(new BouncyCastleProvider());
        MyProperties mp = new MyProperties("./Serveur_Analysis.conf");
        set_codeProvider(mp.getContent("PROVIDER"));
        set_hash(mp.getContent("HASH"));
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public boolean isConnect()
    {
        return _connect;
    }

    public String getLogin()
    {
        return _login;
    }

    public String getPwd()
    {
        return _pwd;
    }

    public String get_codeProvider()
    {
        return _codeProvider;
    }

    public String get_hash()
    {
        return _hash;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setConnect(boolean tmpConnect)
    {
        _connect = tmpConnect;
    }

    public void setLogin(String tmpLogin)
    {
        _login = tmpLogin;
    }

    public void setPwd(String tmpPwd)
    {
        _pwd = tmpPwd;
    }

    public void set_codeProvider(String codeProvider)
    {
        this._codeProvider = codeProvider;
    }

    public void set_hash(String hash)
    {
        this._hash = hash;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private void connectServ()
    {
        MyProperties mp = new MyProperties("./Serveur_Analysis.conf");
        String HOST = mp.getContent("IPSERV");
        int PORT = Integer.parseInt(mp.getContent("PORT_STAT"));

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket(HOST, PORT);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        {
            System.err.println("---connectServ Erreur ! Host non trouvé [" + e + "]");//TODO:quitter
        }
        catch (IOException e)
        {
            System.err.println("---connectServ Erreur ! Pas de connexion ? [" + e + "]");//TODO:quitter
        }
    }

    private void sendReq(RequetePIDEP req)
    {
        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            //pour vider le cache
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("---sendReq Erreur réseau ? [" + e.getMessage() + "]");//TODO:quitter
        }
    }

    private ReponsePIDEP readRep()
    {
        // Lecture de la réponse
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            return (ReponsePIDEP)ois.readObject();
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

    private void closeSocket()
    {
        try
        {
            cliSock.close();
        }
        catch (IOException e)
        {
            System.out.println("---closeSocket erreur IO = " + e.getMessage());
        }
    }

    public ReponsePIDEP sendLogin()
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        closeSocket();

        return rep;
    }

    public ReponsePIDEP sendGetStatDescrCont(int tailEch, boolean entree)
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        if(rep.getCode() == 200) {

            dt = new DonneeGetStatDescrCont(tailEch, entree);

            req = new RequetePIDEP(dt);

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
        }

        closeSocket();

        return null;
    }

    public ReponsePIDEP sendGrCouleurRep(int donnee, boolean annee)
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        if(rep.getCode() == 200) {

            dt = new DonneeGetGrCouleurRep(donnee, annee);

            req = new RequetePIDEP(dt);

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
        }

        closeSocket();

        return null;
    }

    public ReponsePIDEP sendGrCouleurComp(int annee)
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        if(rep.getCode() == 200) {

            dt = new DonneeGetGrCouleurComp(annee);

            req = new RequetePIDEP(dt);

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
        }

        closeSocket();

        return null;
    }

    public ReponsePIDEP sendGetStatInferTestConf(int tailEch)
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        if(rep.getCode() == 200) {

            dt = new DonneeGetStatInferTestConf(tailEch);

            req = new RequetePIDEP(dt);

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
        }

        closeSocket();

        return null;
    }

    public ReponsePIDEP sendGetStatInferTestHomog(int tailEch)
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        if(rep.getCode() == 200) {

            dt = new DonneeGetStatInferTestHomog(tailEch);

            req = new RequetePIDEP(dt);

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
        }

        closeSocket();

        return null;
    }

    public ReponsePIDEP sendGetStatInferANOVA(int tailEch)
    {
        RequetePIDEP req = null;
        ReponsePIDEP rep = null;

        dt = new DonneeLogin();

        createDonneeLogin((DonneeLogin)dt);

        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        if(rep.getCode() == 200) {

            dt = new DonneeGetStatInferANOVA(tailEch);

            req = new RequetePIDEP(dt);

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
        }

        closeSocket();

        return null;
    }

    private void createDonneeLogin(DonneeLogin dt)
    {
        try
        {
            //Déclaration du message digest
            MessageDigest md = MessageDigest.getInstance(get_hash(), get_codeProvider());

            //Création du message digest salé
            md.update(getPwd().getBytes());

            //Création du sel
            long temps = (new Date()).getTime();
            double alea = Math.random();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(temps);
            bdos.writeDouble(alea);

            //Ajout du sel
            md.update(baos.toByteArray());

            dt.setUsername(getLogin());
            dt.setTemps(temps);
            dt.setAlea(alea);
            dt.setMsgD(md.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("Problème d'algorithme : " + e.getMessage());
        }
        catch(NoSuchProviderException e)
        {
            System.out.println("Problème de provider : " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Problème imprévu : " + e.getMessage() + e.getClass());
        }
    }
}