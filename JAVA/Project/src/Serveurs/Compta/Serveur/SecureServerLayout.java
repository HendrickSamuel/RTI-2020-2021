//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package Serveurs.Compta.Serveur;

import Serveurs.Mouvement.Serveur.ConsoleSwing;
import genericRequest.MyProperties;
import lib.BeanDBAcces.BDCompta;
import security.SecurityHelper;

import javax.swing.*;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class SecureServerLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private ServeurComptaSAMOP serveur;
    private ConsoleSwing cs;
    private int port;

    private JPanel mainPanel;
    private JLabel labelPort;
    private JButton buttonStartStop;
    private JTextArea textArea1;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public SecureServerLayout()
    {
        super("Secure Serveur Compta");

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
        buttonStartStop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
            MyProperties mp = new MyProperties("./Confs/Serveur_Compta.conf");
            port = Integer.parseInt(mp.getContent("PORT_SALARY"));
            NbThreads = Integer.parseInt(mp.getContent("NBTREADS_SAMOP"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDCompta bdC = null;
            try
            {
                bdC = new BDCompta(USER,PWD,"bd_compta");
                serveur = new ServeurComptaSAMOP(port, true, NbThreads, bdC, cs);

                SecurityHelper sc = new SecurityHelper();
                sc.initKeyStore("./Confs/ComptaKeyVault", "password");
                serveur.setSecurityHelper(sc);

                serveur.StartSecureServeur();

                labelPort.setText("PORT: " + port);
            }
            catch (SQLException | ClassNotFoundException | IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e)
            {
                cs.Affiche(e.getMessage());
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
        JFrame frame = new SecureServerLayout();
        frame.setVisible(true);
    }
}