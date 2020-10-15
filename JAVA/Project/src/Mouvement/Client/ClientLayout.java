//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Mouvement.Client;


import javax.swing.*;


public class ClientLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private boolean _connect;

    private JPanel mainPanel;
    private JButton buttonLogin;
    private JButton buttonLogout;
    private JButton buttonInputLorry;
    private JButton buttonOperations;
    private JButton buttonInputLorryWithoutRes;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientLayout()
    {
        super("Client Mouvement");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        initComponents();
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public boolean isConnect()
    {
        return _connect;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setConnect(boolean tmpConnect)
    {
        _connect = tmpConnect;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        buttonInputLorry.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onInputLorry(evt);
            }
        });

        buttonInputLorryWithoutRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onInputLorryWithoutRes(evt);
            }
        });

        buttonOperations.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onOperations(evt);
            }
        });

        buttonLogout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLogout(evt);
            }
        });

        buttonLogin.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLogin(evt);
            }
        });
    }

    private void onInputLorry(java.awt.event.ActionEvent evt)
    {

    }

    private void onInputLorryWithoutRes(java.awt.event.ActionEvent evt)
    {

    }

    private void onOperations(java.awt.event.ActionEvent evt)
    {

    }

    private void onLogout(java.awt.event.ActionEvent evt)
    {

    }

    private void onLogin(java.awt.event.ActionEvent evt)
    {
        this.setVisible(false);

        DialogLogin login = new DialogLogin(this, true);
        login.setSize(260, 190);
        login.setVisible(true);

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
