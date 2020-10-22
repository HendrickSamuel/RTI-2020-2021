//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Mouvement.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.TRAMAP.DonneeLogin;
import protocol.TRAMAP.ReponseTRAMAP;
import protocol.TRAMAP.RequeteTRAMAP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
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
    public Client()
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
        int PORT = Integer.parseInt(mp.getContent("PORT1"));

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket(HOST, PORT);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");//TODO:quitter
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");//TODO:quitter
        }
    }

    private void sendReq(RequeteTRAMAP req)
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
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");//TODO:quitter
        }
    }

    private ReponseTRAMAP readRep()
    {
        // Lecture de la réponse
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            return (ReponseTRAMAP)ois.readObject();
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
        }
        return null;
    }


    public ReponseTRAMAP sendLogin()
    {
        dt = new DonneeLogin(getLogin(),getPwd());
        RequeteTRAMAP req = null;
        req = new RequeteTRAMAP(dt);

        connectServ();

        sendReq(req);

        return readRep();
    }

}