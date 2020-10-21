//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Mouvement.Serveur;

import MyGenericServer.ListeTaches;
import MyGenericServer.ThreadServer;
import genericRequest.MyProperties;
import lib.BeanDBAcces.BDMouvements;
import lib.BeanDBAcces.DataSource;

import javax.swing.*;
import java.sql.SQLException;

public class ServerLayout extends JFrame {

    public static void main (String[] args){
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

    private ThreadServer th1;
    private ThreadServer th2;

    private int port1;
    private int port2;

    /********************************/
    /*         Constructeurs        */
    /********************************/

    public ServerLayout()
    {
        super("Serveur Mouvement");

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
        if(th1 == null || !th1.isAlive())
        {
            toggleServer1.setText("-- STOP --");
            textArea1.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea1);
            MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
            int port = Integer.parseInt(mp.getContent("PORT1"));

            ListeTaches lt = new ListeTaches();
            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            DataSource bd = null;
            try {
                bd = new BDMouvements(USER,PWD,"bd_mouvements");
                th1 = new ThreadServer(port, lt, cs, true, "protocolTRAMAP.TraitementTRAMAP", bd);
                th1.start();

                labelPort1.setText("PORT: " + port);
            } catch (SQLException |ClassNotFoundException e) {
                cs.Affiche("Could not find DataBase to start on");
                toggleServer1.setText("-- START --");
            }

        }
        else
        {
            //th1.interrupt();
            th1.StopServeur();
            try {
                th1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toggleServer1.setText("-- START --");
        }
    }

    private void ToggleServer2(java.awt.event.ActionEvent evt)
    {
        if(th2 == null || !th2.isAlive())
        {
            toggleServer2.setText("-- STOP --");
            textArea2.setText("");
            ConsoleSwing cs = new ConsoleSwing(textArea2);
            MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
            int port = Integer.parseInt(mp.getContent("PORT2"));
            labelPort2.setText("PORT: " + port);
            ListeTaches lt = new ListeTaches();
            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            DataSource bd = null;
            try {
                bd = new BDMouvements(USER,PWD,"bd_mouvements");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ;
            th2 = new ThreadServer(port, lt, cs, true, "protocolTRAMAP.TraitementTRAMAP", bd);
            th2.start();
        }
        else
        {
            //th1.interrupt();
            th2.StopServeur();
            try {
                th2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toggleServer2.setText("-- START --");
        }
    }
}
