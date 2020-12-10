//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Serveurs;


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

public class ServeursLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private ServeurTCP serveur1;

    private int port1;

    private JPanel mainPannel;
    private JTextArea textArea1;
    private JButton toggleServer1;
    private JLabel labelPort1;
    private JLabel labelServer1;


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

        this.setSize(500, 500);

        toggleServer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleServer1(evt);
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
            port1 = Integer.parseInt(mp.getContent("PORT_FOR_US_ONLY"));
            NbThreads = Integer.parseInt(mp.getContent("NBTHREADS"));

            String USER = mp.getContent("BDUSER");
            String PWD = mp.getContent("BDPWD");
            BDCompta bd = null;
            try
            {
                bd = new BDCompta(USER,PWD,"bd_compta");
                serveur1 = new ServeurTCP(port1, true, NbThreads, bd, cs, false);
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
            }
            catch (IOException e)
            {
                cs.Affiche(e.getMessage());
            }
            catch (CertificateException e)
            {
                cs.Affiche(e.getMessage());
            }
            catch (NoSuchAlgorithmException e)
            {
                cs.Affiche(e.getMessage());
            }
            catch (KeyStoreException e)
            {
                cs.Affiche(e.getMessage());
            }
        }
        else
        {
            serveur1.StopServeur();
            serveur1 = null;
            toggleServer1.setText("-- START --");
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