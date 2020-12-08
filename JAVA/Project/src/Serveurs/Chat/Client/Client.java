//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Client;

import genericRequest.MyProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocol.BISAMAP.RequeteBISAMAP;
import protocol.PFMCOP.ReponsePFMCOP;
import protocol.PFMCOP.RequetePFMCOP;
import protocol.PIDEP.ReponsePIDEP;
import security.SecurityHelper;

import java.io.*;
import java.net.Socket;
import java.security.Security;

public class Client
{
    /********************************/
    /*           Variables          */
    /********************************/
    private SecurityHelper _securityHelper;
    private boolean _connected;
    private String _login;
    private String _pasword;

    private String _serverIp;
    private int _port;

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket cliSock;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Client()
    {
        Security.addProvider(new BouncyCastleProvider());
        MyProperties mp = new MyProperties("./Confs/Serveur_Chat.conf");
        _port = Integer.parseInt(mp.getContent("PORT1"));
        _serverIp = mp.getContent("IPSERV");

        _securityHelper = new SecurityHelper();
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

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_connected(boolean con)
    {
        _connected = con;
    }

    public void set_login(String log)
    {
        _login = log;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private boolean connectToServer()
    {
        dis=null;
        dos=null;
        cliSock = null;

        try
        {
            cliSock = new Socket(_serverIp, _port);
            System.out.println(cliSock.getInetAddress().toString());
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendReq(RequetePFMCOP req, int type)
    {
        try
        {
            dos = new DataOutputStream(cliSock.getOutputStream());
            dos.write(type);
            //pour vider le cache
            dos.writeBytes(req.toString());
            dos.flush();
            return true;
        }
        catch (IOException e)
        {
            System.err.println("---sendReq Erreur réseau ? [" + e.getMessage() + "]");
            return false;
        }
    }

    private ReponsePFMCOP readRep()
    {
        // Lecture de la réponse
        /*try
        {
            dis = new DataInputStream(cliSock.getInputStream());
            int type = dis.read();
            switch (type)
            {
                case 1:
                    (ReponsePFMCOP)dis.readDouble();
                    return null;
                    break;
            }

        }
        catch (ClassNotFoundException e)
        {
            System.out.println("---readRep erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("---readRep erreur IO = " + e.getMessage());
        }*/
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
}