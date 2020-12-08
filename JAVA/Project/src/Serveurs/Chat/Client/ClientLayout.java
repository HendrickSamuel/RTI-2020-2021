//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Client;


import Serveurs.Mouvement.Serveur.ConsoleSwing;
import javax.swing.*;


public class ClientLayout  extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Client _client;
    private JPanel mainPanel;
    private JButton buttonEnvoyer;
    private JTextArea textArea1;
    private JTextField messageField;

    private ConsoleSwing cs;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientLayout()
    {
        super("Client Serveurs.Chat");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        initComponents();

        setClient(new Client());
        setConnect(false);
        cs = new ConsoleSwing(textArea1);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public Client getClient()
    {
        return _client;
    }

    public boolean isConnect()
    {
        return _client.is_connected();
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setClient(Client tmpClient)
    {
        _client = tmpClient;
    }

    public void setConnect(boolean tmpConnect)
    {
        _client.set_connected(tmpConnect);
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        buttonEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnvoyerMsg(evt);
            }
        });
    }

    private void EnvoyerMsg(java.awt.event.ActionEvent evt)
    {
        cs.Affiche("Envoyer");
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = new ClientLayout();
        frame.setVisible(true);
    }
}