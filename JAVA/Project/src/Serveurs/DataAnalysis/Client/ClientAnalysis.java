//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.PIDEP.ReponsePIDEP;
import protocol.PIDEP.RequetePIDEP;
import protocol.TRAMAP.DonneeLogin;
import protocol.TRAMAP.ReponseTRAMAP;
import protocol.TRAMAP.RequeteTRAMAP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientAnalysis
{
    /********************************/
    /*           Variables          */
    /********************************/
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
        MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
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

        dt = new DonneeLogin(getLogin(),getPwd());
        req = new RequetePIDEP(dt);

        connectServ();

        sendReq(req);

        rep = readRep();

        closeSocket();

        return rep;
    }
}