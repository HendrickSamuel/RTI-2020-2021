//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 17/12/2020

package Serveurs.Admin;


import protocol.CSA.ReponseCSA;
import protocol.PFMCOP.ReponsePFMCOP;

import javax.swing.*;

public class AdminLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Admin _admin;
    private ReponseCSA rc;

    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel appPanel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton buttonEnvoyer;
    private JRadioButton clientsRadio;
    private JRadioButton stopRadio;
    private JButton submitButton;
    private JTextField secField;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public AdminLayout()
    {
        super("Admin Serveur container");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        set_admin(new Admin());

        initComponents();

    }


    /********************************/
    /*            Getters           */
    /********************************/
    public Admin get_admin()
    {
        return _admin;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_admin(Admin _admin)
    {
        this._admin = _admin;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        appPanel.setVisible(false);

        buttonEnvoyer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                LoginMsg(evt);
            }
        });

        submitButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                requeteMsg(evt);
            }
        });
    }

    private void LoginMsg(java.awt.event.ActionEvent evt)
    {
        get_admin().setLogin(usernameField.getText());
        get_admin().setPwd(passwordField.getText());

        rc = get_admin().sendLoginA();

        if(rc.getCode() == ReponsePFMCOP.OK)
        {
            loginPanel.setVisible(false);
            appPanel.setVisible(true);
        }
    }

    private void requeteMsg(java.awt.event.ActionEvent evt)
    {
        if(clientsRadio.isSelected())
        {
            rc = get_admin().sendClients();
        }
        else if(stopRadio.isSelected())
        {
            get_admin().setSec(Integer.parseInt(secField.getText()));
            rc = get_admin().sendStop();
        }
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = new AdminLayout();
        frame.setVisible(true);
    }
}