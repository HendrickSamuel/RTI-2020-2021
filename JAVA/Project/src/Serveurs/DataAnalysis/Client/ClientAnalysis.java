//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.PIDEP.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

public class ClientAnalysis
{
    /********************************/
    /*           Variables          */
    /********************************/
    private static String codeProvider = "BC";
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

        try
        {
            //Déclaration du message digest
            MessageDigest md = MessageDigest.getInstance("SHA-256", codeProvider);

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

            ((DonneeLogin)dt).setUsername(getLogin());
            ((DonneeLogin)dt).setTemps(temps);
            ((DonneeLogin)dt).setAlea(alea);
            ((DonneeLogin)dt).setMsgD(md.digest());

            req = new RequetePIDEP(dt);

            connectServ();

            sendReq(req);

            rep = readRep();

            closeSocket();

            return rep;
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
        return null;
    }
}