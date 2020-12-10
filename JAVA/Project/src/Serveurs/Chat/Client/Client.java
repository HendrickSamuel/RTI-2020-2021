//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Client;

import Serveurs.Mouvement.Serveur.ConsoleSwing;
import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocol.PFMCOP.*;
import security.SecurityHelper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;
import java.util.Random;


public class Client
{
    /********************************/
    /*           Variables          */
    /********************************/
    private SecurityHelper _securityHelper;
    private boolean _connected;
    private String _login;
    private String _password;

    private String _serverIp;
    private int _port;

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket cliSock;
    private DonneeRequete dt;

    private InetAddress adresseGroupe;
    private MulticastSocket socketGroupe;
    private ThreadReception thr;

    private ConsoleSwing _cs;
    private DefaultListModel _jl;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Client()
    {
        Security.addProvider(new BouncyCastleProvider());
        MyProperties mp = new MyProperties("./Confs/Serveur_Chat.conf");
        _port = Integer.parseInt(mp.getContent("PORT_FOR_US_ONLY"));
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

    public String get_pwd()
    {
        return _password;
    }

    public ConsoleSwing get_cs()
    {
        return _cs;
    }

    public DefaultListModel get_jl()
    {
        return _jl;
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

    public void set_pwd(String pwd)
    {
        _password = pwd;
    }

    public void set_cs(ConsoleSwing cs)
    {
        _cs = cs;
    }

    public void set_jl(DefaultListModel _jl)
    {
        this._jl = _jl;
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

    private boolean sendReq(RequetePFMCOP req)
    {
        try
        {
            dos = new DataOutputStream(cliSock.getOutputStream());
            System.out.println(req.toString());
            System.out.println("digest : " + ((DonneeLoginGroup)req.getChargeUtile()).get_pwdDigest());
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

    private ReponsePFMCOP readRep()
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

    public ReponsePFMCOP sendLoginGroup()
    {
        RequetePFMCOP req = null;
        ReponsePFMCOP rep = null;

        try
        {
            dt = new DonneeLoginGroup();
            ((DonneeLoginGroup)dt).set_username(get_login());
            ((DonneeLoginGroup)dt).set_temps((new Date()).getTime());
            ((DonneeLoginGroup)dt).set_aleatoire(Math.random());
            ((DonneeLoginGroup)dt).set_pwdDigest(_securityHelper.createSaltedDigest(get_pwd(), ((DonneeLoginGroup) dt).get_temps(), ((DonneeLoginGroup) dt).get_aleatoire()));

            req = new RequetePFMCOP(dt);

            if(cliSock == null)
                connectToServer();

            sendReq(req);
            rep = readRep();
        }
        catch (NoSuchProviderException | NoSuchAlgorithmException | IOException e)
        {
            e.printStackTrace();
        }

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

    public ReponsePFMCOP parseString(String message)
    {
        String[] parametres = message.split("#");
        String[] row;
        System.out.println("Objet reçu: " + parametres[0]);
        try
        {
            ReponsePFMCOP rp = (ReponsePFMCOP)Class.forName(parametres[0]).newInstance();
            rp.setFiledsFromString(message);
            return rp;
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void ConnectUDP(int port)
    {
        closeSocket();

        MyProperties mp = new MyProperties("./Confs/Serveur_Chat.conf");
        _serverIp = mp.getContent("IPCHAT");
        _port = port;

        System.out.println("adresse = " + _serverIp);
        System.out.println("port = " + _port);

        RequetePFMCOP req = null;

        try
        {
            adresseGroupe = InetAddress.getByName(_serverIp);
            socketGroupe = new MulticastSocket(_port);
            socketGroupe.joinGroup(adresseGroupe);
            thr = new ThreadReception (get_login(), socketGroupe, _cs, get_jl());
            thr.set_enMarche(true);
            thr.start();

            dt = new DonneeBaseUDP();
            ((DonneeBaseUDP)dt).set_username(get_login() + " rejoint le groupe");

            req = new RequetePFMCOP(dt);
            DatagramPacket dtg = new DatagramPacket(req.toString().getBytes(), req.toString().length(), adresseGroupe, _port);
            socketGroupe.send(dtg);
        }
        catch (IOException e)
        {
            get_cs().Affiche("Erreur :-( : " + e.getMessage());
        }
    }

    public void QuitUDP()
    {
        RequetePFMCOP req = null;

        try
        {
            dt = new DonneeBaseUDP();
            ((DonneeBaseUDP)dt).set_username(get_login() + " quitte le groupe");

            req = new RequetePFMCOP(dt);
            DatagramPacket dtg = new DatagramPacket(req.toString().getBytes(), req.toString().length(), adresseGroupe, _port);

            socketGroupe.send(dtg);
            thr.set_enMarche(false);
            socketGroupe.leaveGroup(adresseGroupe);
            System.out.println("Après leaveGroup");
            socketGroupe.close();
            System.out.println("Après close");

            MyProperties mp = new MyProperties("./Confs/Serveur_Chat.conf");
            _port = Integer.parseInt(mp.getContent("PORT_FOR_US_ONLY"));
            _serverIp = mp.getContent("IPSERV");
        }
        catch (IOException e)
        {
            System.out.println("Erreur :-( : " + e.getMessage());
        }
    }

    public void SendPostQuestion(String message)
    {
        RequetePFMCOP req = null;

        try
        {
            String tag = GenString(5);
            dt = new DonneePostQuestion();
            ((DonneePostQuestion)dt).set_user(get_login());
            ((DonneePostQuestion)dt).setTag(tag);
            ((DonneePostQuestion)dt).set_msgDigest(_securityHelper.createDigest(message));
            ((DonneePostQuestion)dt).set_message(message);

            req = new RequetePFMCOP(dt);
            DatagramPacket dtg = new DatagramPacket(req.toString().getBytes(), req.toString().length(), adresseGroupe, _port);
            socketGroupe.send(dtg);
        }
        catch (IOException | NoSuchProviderException | NoSuchAlgorithmException e)
        {
            get_cs().Affiche(e.getMessage());
        }
    }

    public void SendAnswerQuestion(String message, Question quest)
    {
        RequetePFMCOP req = null;

        try
        {
            if(_securityHelper.CompareSimpleDigests(quest.get_msgDigest(), quest.get_question().getBytes()))
            {
                dt = new DonneeAnswerQuestion();
                ((DonneeAnswerQuestion)dt).set_user(get_login());
                ((DonneeAnswerQuestion)dt).set_tag(quest.get_tag());
                ((DonneeAnswerQuestion)dt).set_message(message);

                req = new RequetePFMCOP(dt);
                DatagramPacket dtg = new DatagramPacket(req.toString().getBytes(), req.toString().length(), adresseGroupe, _port);
                socketGroupe.send(dtg);
            }
        }
        catch (NoSuchProviderException | NoSuchAlgorithmException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public void SendPostEvent(String message)
    {
        RequetePFMCOP req = null;

        try
        {
            String tag = GenString(8);
            dt = new DonneePostEvent();
            ((DonneePostEvent)dt).set_user(get_login());
            ((DonneePostEvent)dt).set_tag(tag);
            ((DonneePostEvent)dt).set_message(message);

            req = new RequetePFMCOP(dt);
            DatagramPacket dtg = new DatagramPacket(req.toString().getBytes(), req.toString().length(), adresseGroupe, _port);
            socketGroupe.send(dtg);
        }
        catch (IOException e)
        {
            get_cs().Affiche(e.getMessage());
        }
    }

    public String GenString(int len)
    {
        int leftLimit = 97; // 'a'
        int rightLimit = 122; // 'z'
        int targetStringLength = len;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();

        return generatedString;
    }
}