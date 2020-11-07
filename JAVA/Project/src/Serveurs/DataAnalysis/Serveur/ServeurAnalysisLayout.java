//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Serveur;

import Serveurs.Mouvement.Serveur.ConsoleSwing;
import genericRequest.MyProperties;
import genericRequest.RServe;
import lib.BeanDBAcces.BDDecisions;
import lib.BeanDBAcces.BDMouvements;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class ServeurAnalysisLayout extends JFrame
{

    /********************************/
    /*           Variables          */
    /********************************/
    private ServeurDataAnalysis serveur;
    private Process process;
    private RServe rServe;
    private ConsoleSwing cs;
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

            cs = new ConsoleSwing(textArea1);
            MyProperties mp = new MyProperties("./Serveur_Analysis.conf");
            port = Integer.parseInt(mp.getContent("PORT_STAT"));
            NbThreads = Integer.parseInt(mp.getContent("NBTHREADS_STAT"));
            String pathR = mp.getContent("RSERVEPATH");
            String hostR = mp.getContent("RSERVE");


            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDMouvements bdM = null;
            BDDecisions bdD = null;
            try
            {
                bdM = new BDMouvements(USER,PWD,"bd_mouvements");
                bdD = new BDDecisions(USER,PWD,"bd_decisions");
                process = new ProcessBuilder(pathR).start();
                cs.Affiche("Demarrage du serveur Rserve");
                rServe = new RServe(hostR);
                cs.Affiche("Connexion au serveur Rserve");
                serveur = new ServeurDataAnalysis(port, true, NbThreads, bdM, bdD, rServe, cs);
                serveur.StartServeur();

                labelPort.setText("PORT: " + port);
            }
            catch (SQLException | ClassNotFoundException | IOException e)
            {
                cs.Affiche(e.getMessage());
                buttonStartStop.setText("-- START --");
            }
        }
        else
        {
            serveur.StopServeur();
            serveur = null;
            rServe.RserveClose();
            cs.Affiche("Deconnexion du serveur Rserve");
            process.destroy();
            process = null;
            cs.Affiche("Arret du serveur Rserve");
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