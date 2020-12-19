//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Client;


import Serveurs.Mouvement.Serveur.ConsoleSwing;
import protocol.PFMCOP.DonneeLoginGroup;
import protocol.PFMCOP.ReponsePFMCOP;
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
    private JPanel chatPanel;
    private JPanel loginPanel;
    private JTextField userField;
    private JTextField passField;
    private JButton validerButton;
    private JLabel labelError;
    private JButton quitterButton;
    private JRadioButton radioPost;
    private JRadioButton radioEvent;
    private JRadioButton radioAnswer;
    private JList list1;

    private ConsoleSwing cs;
    private ReponsePFMCOP rp;


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
        getClient().set_cs(cs);
        DefaultListModel dlm = new DefaultListModel();
        list1.setModel(dlm);
        getClient().set_jl(dlm);
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
        chatPanel.setVisible(false);

        buttonEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnvoyerMsg(evt);
            }
        });

        validerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnvoyerConnexion(evt);
            }
        });

        quitterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitterChat(evt);
            }
        });
    }

    private void EnvoyerMsg(java.awt.event.ActionEvent evt)
    {
        if(messageField.getText().length() > 0)
        {
            if (radioPost.isSelected())
            {
                getClient().SendPostQuestion(messageField.getText());
                messageField.setText("");
            }
            else if (radioAnswer.isSelected())
            {
                if (list1.getModel().getSize() > 0 && list1.getSelectedValue() != null)
                {
                    getClient().SendAnswerQuestion(messageField.getText(), (Question) list1.getSelectedValue());
                    messageField.setText("");
                }
            }
            else if (radioEvent.isSelected())
            {
                getClient().SendPostEvent(messageField.getText());
                messageField.setText("");
            }
        }
    }

    private void QuitterChat(java.awt.event.ActionEvent evt)
    {
        getClient().QuitUDP();
        loginPanel.setVisible(true);
        chatPanel.setVisible(false);
    }

    private void EnvoyerConnexion(java.awt.event.ActionEvent evt)
    {
        getClient().set_login(userField.getText());
        getClient().set_pwd(passField.getText());

        rp = getClient().sendLoginGroup();

        if(rp.getCode() != ReponsePFMCOP.OK)
        {
            labelError.setText(rp.getMessage());
        }
        else
        {
            labelError.setText("");
            loginPanel.setVisible(false);
            chatPanel.setVisible(true);
            getClient().ConnectUDP(((DonneeLoginGroup)rp.getChargeUtile()).get_port());
        }
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