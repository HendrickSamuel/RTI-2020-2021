//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 17/12/2020

package Serveurs.Admin;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.CSA.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class Admin
{
    /********************************/
    /*           Variables          */
    /********************************/
    private boolean _connect;
    private String _login;
    private String _pwd;
    private int _sec;

    private String _serverIp;
    private int _port;

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket cliSock;
    private DonneeRequete dt;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Admin()
    {
        MyProperties mp = new MyProperties("./Confs/Serveur_Mouvement.conf");
        _port = Integer.parseInt(mp.getContent("PORT_ADMIN"));
        _serverIp = mp.getContent("IPSERV2");
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

    public int getSec()
    {
        return _sec;
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

    public void setSec(int tmpSec)
    {
        _sec = tmpSec;
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

    private boolean sendReq(RequeteCSA req)
    {
        try
        {
            dos = new DataOutputStream(cliSock.getOutputStream());
            dos.write(req.toString().getBytes());

            //pour vider le cache
            dos.flush();
            return true;
        }
        catch (IOException e)
        {
            System.err.println("---sendReq Erreur réseau ? [" + e.getMessage() + "]");
            return false;
        }
    }

    private ReponseCSA readRep()
    {
        // Lecture de la réponse
        try
        {
            dis = new DataInputStream(cliSock.getInputStream());
            System.out.println("Avant read allBytes");
            String rep = readAllBytes(dis);
            System.out.println("Apres read allBytes et avant parseString");
            return parseString(rep);

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
            cliSock = null;
        }
        catch (IOException e)
        {
            System.out.println("---closeSocket erreur IO = " + e.getMessage());
        }
    }

    public ReponseCSA sendLoginA()
    {
        RequeteCSA req = null;
        ReponseCSA rep = null;

        dt = new DonneeLoginA();
        ((DonneeLoginA)dt).set_username(getLogin());
        ((DonneeLoginA)dt).set_password(getPwd());

        req = new RequeteCSA(dt);

        if(cliSock == null)
            connectToServer();

        sendReq(req);
        rep = readRep();

        return rep;
    }

    public ReponseCSA sendClients()
    {
        RequeteCSA req = null;
        ReponseCSA rep = null;

        dt = new DonneeLCients();

        req = new RequeteCSA(dt);

        if(cliSock == null)
            connectToServer();

        sendReq(req);
        rep = readRep();

        return rep;
    }

    public ReponseCSA sendStop()
    {
        RequeteCSA req = null;
        ReponseCSA rep = null;

        dt = new DonneeStop();
        ((DonneeStop)dt).set_secondes(getSec());

        req = new RequeteCSA(dt);

        if(cliSock == null)
            connectToServer();

        sendReq(req);
        rep = readRep();

        return rep;
    }

    public String readAllBytes(DataInputStream dis) throws IOException
    {
        StringBuffer message = new StringBuffer();
        byte b = 0;
        message.setLength(0);
        while((b = dis.readByte()) != (byte)'\n')
        {
            if(b != '\n')
                message.append((char)b);
        }
        return message.toString().trim();
    }

    public ReponseCSA parseString(String message)
    {
        String[] parametres = message.split("#");
        String[] row;
        System.out.println("Objet reçu: " + parametres[0]);
        try
        {
            ReponseCSA rp = (ReponseCSA)Class.forName(parametres[0]).newInstance();
            rp.setFiledsFromString(message);
            return rp;
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}