//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Serveurs.Compta.Serveur;

import MyGenericServer.ThreadServer;
import Serveurs.Mouvement.Serveur.ConsoleSwing;
import genericRequest.MyProperties;
import lib.BeanDBAcces.BDMouvements;
import security.SecurityHelper;

import javax.swing.*;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class ServerLayout extends JFrame
{

    public static void main (String[] args)
    {
        JFrame frame = new ServerLayout();
        frame.setVisible(true);
    }


    /********************************/
    /*           Variables          */
    /********************************/
    private JButton toggleServer1;
    private JButton toggleServer2;
    private JLabel labelServer1;
    private JLabel labelServer2;
    private JLabel labelPort1;
    private JLabel labelPort2;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JPanel mainPannel;

    private ServeurComptaBISAMAP serveur1;
    private ServeurComptaCHAMAP serveur2;

    private int port1;
    private int port2;

    /********************************/
    /*         Constructeurs        */
    /********************************/

    public ServerLayout()
    {
        super("Serveur Serveurs.Mouvement");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setExtendedState(JFrame.MAXIMIZED_VERT);
        this.setContentPane(this.mainPannel);
        this.pack();

        toggleServer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleServer1(evt);
            }
        });

        toggleServer2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleServer2(evt);
            }
        });
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

    private void ToggleServer1(java.awt.event.ActionEvent evt)
    {
        if(serveur1 == null)
        {
            int NbThreads = 0;
            toggleServer1.setText("-- STOP --");
            textArea1.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea1);
            MyProperties mp = new MyProperties("./Confs/Serveur_Compta.conf"); //todo: changer
            port1 = Integer.parseInt(mp.getContent("PORT_BISAMAP"));
            NbThreads = Integer.parseInt(mp.getContent("NBTREADS_BISAMAP"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDMouvements bd = null;
            try
            {
                bd = new BDMouvements(USER,PWD,"bd_compta");
                serveur1 = new ServeurComptaBISAMAP(port1, true, NbThreads, cs, true);
                serveur1.setBd_comtpa(bd);
                SecurityHelper sc = new SecurityHelper();
                sc.initKeyStore("./Confs/ComptaKeyVault", "password");
                serveur1.setSecurityHelper(sc);
                serveur1.StartServeur();

                labelPort1.setText("PORT: " + port1);
            }
            catch (SQLException |ClassNotFoundException e)
            {
                cs.Affiche("Could not find DataBase to start on");
                toggleServer1.setText("-- START --");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }
        else
        {
            serveur1.StopServeur();
            serveur1 = null;
            toggleServer1.setText("-- START --");
        }
    }

    private void ToggleServer2(java.awt.event.ActionEvent evt)
    {
        /*
        if(serveur2 == null)
        {
            int NbThreads = 0;
            toggleServer2.setText("-- STOP --");
            textArea2.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea2);
            MyProperties mp = new MyProperties("./Confs/Serveur_Mouvement.conf");
            port2 = Integer.parseInt(mp.getContent("PORT2"));
            NbThreads = Integer.parseInt(mp.getContent("NBTHREADS_PORT2"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDMouvements bd = null;
            try
            {
                bd = new BDMouvements(USER,PWD,"bd_mouvements");
                serveur2 = new ServeurComptaCHAMAP(port2, true, NbThreads, bd, cs, false);
                serveur2.StartServeur();

                labelPort2.setText("PORT: " + port2);
            }
            catch (SQLException |ClassNotFoundException e)
            {
                cs.Affiche("Could not find DataBase to start on");
                toggleServer2.setText("-- START --");
            }

        }
        else
        {
            serveur2.StopServeur();
            serveur2 = null;
            toggleServer2.setText("-- START --");
        }
        */
    }
}
