//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Serveur;

import Serveurs.Mouvement.Serveur.ConsoleSwing;
import genericRequest.MyProperties;
import lib.BeanDBAcces.BDMouvements;

import javax.swing.*;
import java.sql.SQLException;

public class ServeurAnalysisLayout extends JFrame
{

    /********************************/
    /*           Variables          */
    /********************************/
    private ServeurDataAnalysis serveur;
    private int port;

    private JTextArea textArea1;
    private JButton buttonStartStop;
    private JPanel mainPanel;
    private JLabel labelPort;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurAnalysisLayout()
    {
        super("Serveur Analysis");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.setLocationRelativeTo(null);
        this.pack();


        initComponents();
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
    private void initComponents()
    {
        buttonStartStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SartStopServer(evt);
            }
        });
    }

    private void SartStopServer(java.awt.event.ActionEvent evt)
    {
        if(serveur == null)
        {
            int NbThreads = 0;
            buttonStartStop.setText("-- STOP --");
            textArea1.setText(null);

            ConsoleSwing cs = new ConsoleSwing(textArea1);
            MyProperties mp = new MyProperties("./Serveur_Analysis.conf");
            port = Integer.parseInt(mp.getContent("PORT_STAT"));
            NbThreads = Integer.parseInt(mp.getContent("NBTHREADS_STAT"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDMouvements bd = null;
            try
            {
                bd = new BDMouvements(USER,PWD,"bd_mouvements");
                serveur = new ServeurDataAnalysis(port, true, NbThreads, bd, cs);
                serveur.StartServeur();

                labelPort.setText("PORT: " + port);
            }
            catch (SQLException |ClassNotFoundException e)
            {
                cs.Affiche("Could not find DataBase to start on");
                buttonStartStop.setText("-- START --");
            }
        }
        else
        {
            serveur.StopServeur();
            serveur = null;
            buttonStartStop.setText("-- START --");
        }
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = new ServeurAnalysisLayout();
        frame.setVisible(true);
    }
}