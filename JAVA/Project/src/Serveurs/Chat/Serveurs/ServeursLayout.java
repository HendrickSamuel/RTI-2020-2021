//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Serveurs;


import Serveurs.Mouvement.Serveur.ConsoleSwing;
import genericRequest.MyProperties;
import lib.BeanDBAcces.BDCompta;
import javax.swing.*;
import java.sql.SQLException;

public class ServeursLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private ServeurTCP serveur1;
    private ServeurUDP serveur2;

    private int port1;
    private int port2;

    private JPanel mainPannel;
    private JTextArea textArea1;
    private JButton toggleServer1;
    private JLabel labelServer1;
    private JLabel labelPort1;
    private JTextArea textArea2;
    private JButton toggleServer2;
    private JLabel labelPort2;
    private JLabel labelServer2;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeursLayout()
    {
        super("Serveur Serveurs.Chat");

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
            MyProperties mp = new MyProperties("./Confs/Serveur_Chat.conf");
            port1 = Integer.parseInt(mp.getContent("PORT1"));
            NbThreads = Integer.parseInt(mp.getContent("NBTHREADS_PORT1"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDCompta bd = null;
            try
            {
                bd = new BDCompta(USER,PWD,"bd_compta");
                serveur1 = new ServeurTCP(port1, true, NbThreads, bd, cs, false);
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
            serveur1 = null;
            toggleServer1.setText("-- START --");
        }
    }

    private void ToggleServer2(java.awt.event.ActionEvent evt)
    {
        if(serveur2 == null)
        {
            int NbThreads = 0;
            toggleServer2.setText("-- STOP --");
            textArea2.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea2);
            MyProperties mp = new MyProperties("./Confs/Serveur_Chat.conf");
            port2 = Integer.parseInt(mp.getContent("PORT2"));
            //NbThreads = Integer.parseInt(mp.getContent("NBTHREADS_PORT2"));

            /*try
            {
                serveur2 = new ServeurUDP(port2, true, NbThreads, cs, false);
                serveur2.StartServeur();

                labelPort2.setText("PORT: " + port2);
            }
            catch (ClassNotFoundException e)
            {
                cs.Affiche("Could not find DataBase to start on");
                toggleServer2.setText("-- START --");
            }*/

        }
        else
        {
            //serveur2.StopServeur();
            serveur2 = null;
            toggleServer2.setText("-- START --");
        }
    }


    /********************************/
    /*              Main            */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = new ServeursLayout();
        frame.setVisible(true);
    }
}