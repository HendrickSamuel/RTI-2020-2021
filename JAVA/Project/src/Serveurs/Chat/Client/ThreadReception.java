//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 10/12/2020

package Serveurs.Chat.Client;

import MyGenericServer.ConsoleServeur;
import protocol.PFMCOP.*;
import security.SecurityHelper;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ThreadReception extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    private boolean  _enMarche;
    private String _nom;
    private MulticastSocket _socketGroupe;
    private ConsoleServeur _cs;
    private DefaultListModel _jlist;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadReception(String n, MulticastSocket ms, ConsoleServeur cs, DefaultListModel l)
    {
        _nom = n;
        _socketGroupe = ms;
        _cs = cs;
        _jlist = l;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public boolean is_enMarche()
    {
        return _enMarche;
    }

    public String get_nom()
    {
        return _nom;
    }

    public MulticastSocket get_socketGroupe()
    {
        return _socketGroupe;
    }

    public ConsoleServeur get_cs()
    {
        return _cs;
    }

    public DefaultListModel get_jlist()
    {
        return _jlist;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_enMarche(boolean _enMarche)
    {
        this._enMarche = _enMarche;
    }

    public void set_nom(String _nom)
    {
        this._nom = _nom;
    }

    public void set_socketGroupe(MulticastSocket _socketGroupe)
    {
        this._socketGroupe = _socketGroupe;
    }

    public void set_cs(ConsoleServeur cs)
    {
        this._cs = cs;
    }

    public void set_jlist(DefaultListModel _jlist)
    {
        this._jlist = _jlist;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    public void run()
    {

        while (is_enMarche())
        {
            try
            {
                byte[] buf = new byte[1000];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                get_socketGroupe().receive(dtg);

                RequetePFMCOP rp = new RequetePFMCOP();
                System.out.println(new String (buf).trim());
                rp.setFiledsFromString(new String (buf).trim());

                if(rp.getChargeUtile() instanceof DonneePostQuestion)
                {
                    TraitePostQuestion((DonneePostQuestion) rp.getChargeUtile());
                }
                else if(rp.getChargeUtile() instanceof DonneePostEvent)
                {
                    TraitePostEvent((DonneePostEvent) rp.getChargeUtile());
                }
                else if(rp.getChargeUtile() instanceof DonneeAnswerQuestion)
                {
                    TraiteAnswerQuestion((DonneeAnswerQuestion) rp.getChargeUtile());
                }
                else if(rp.getChargeUtile() instanceof DonneeBaseUDP)
                {
                    TraiteBaseUDP((DonneeBaseUDP) rp.getChargeUtile());
                }
            }
            catch (IOException | IllegalAccessException | InstantiationException | ClassNotFoundException e)
            {
                get_cs().Affiche("Erreur dans le thread :-( :" + e.getMessage());
                set_enMarche(false);     // fin
            }
        }
    }

    private void TraitePostQuestion(DonneePostQuestion dpq)
    {
        get_cs().Affiche(dpq.get_user() + " : " + dpq.get_message());
        get_jlist().addElement(new Question(dpq.getTag(), dpq.get_message(), dpq.get_msgDigest()));
    }

    private void TraitePostEvent(DonneePostEvent dpe)
    {
        get_cs().Affiche("/!\\EVENT [" + dpe.get_tag() + "] " +  dpe.get_user() + " : " + dpe.get_message());
    }

    private void TraiteAnswerQuestion(DonneeAnswerQuestion dpq)
    {
        get_cs().Affiche(dpq.get_user() + " --> " + dpq.get_tag() + " : " + dpq.get_message());
    }

    private void TraiteBaseUDP(DonneeBaseUDP dbu)
    {
        get_cs().Affiche(dbu.get_username());
    }
}