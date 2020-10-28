//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Serveur;

import MyGenericServer.ThreadServer;
import genericRequest.MyProperties;
import lib.BeanDBAcces.BDMouvements;

import javax.swing.*;
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

    private ServeurMouvement serveur1;
    private ServeurMouvementPLAMAP serveur2;
    private ThreadServer th2;

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
            toggleServer1.setText("-- STOP --");
            textArea1.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea1);
            MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
            port1 = Integer.parseInt(mp.getContent("PORT1"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDMouvements bd = null;
            try
            {
                bd = new BDMouvements(USER,PWD,"bd_mouvements");
                serveur1 = new ServeurMouvement(port1, true, 3, bd, cs);
                serveur1.StartServeur();

                labelPort1.setText("PORT: " + port1);
            }
            catch (SQLException |ClassNotFoundException e)
            {
                cs.Affiche("Could not find DataBase to start on");
                toggleServer1.setText("-- START --");
            }

        }
        else
        {
            serveur1.StopServeur();
            toggleServer1.setText("-- START --");
        }
    }

    private void ToggleServer2(java.awt.event.ActionEvent evt)
    {
        if(serveur2 == null)
        {
            toggleServer2.setText("-- STOP --");
            textArea2.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea2);
            MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
            port2 = Integer.parseInt(mp.getContent("PORT2"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDMouvements bd = null;
            try
            {
                bd = new BDMouvements(USER,PWD,"bd_mouvements");
                serveur2 = new ServeurMouvementPLAMAP(port2, true, 3, bd, cs, false);
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
            toggleServer2.setText("-- START --");
        }
    }
}
