//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Client;

import protocol.TRAMAP.ReponseTRAMAP;

import javax.swing.*;
import java.awt.event.*;

public class DialogLogin extends javax.swing.JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Client _client;
    private boolean _loginValide;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private JTextField passwordField;
    private JLabel passwordLabel;
    private JLabel loginLabel;
    private JLabel labelError;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogLogin()
    {
        initComponents();

        this.setTitle("Login Serveurs.Mouvement");
        this.setLocationRelativeTo(null);
        setLoginValide(false);
    }

    public DialogLogin(java.awt.Frame parent, boolean modal, Client client)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);
        setLoginValide(false);

        setClient(client);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public Client getClient()
    {
        return _client;
    }

    public String getUtilisateur()
    {
        return getClient().getLogin();
    }

    public String getPwd()
    {
        return getClient().getPwd();
    }

    public boolean getLoginValide()
    {
        return _loginValide;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setClient(Client tmpClient)
    {
        _client = tmpClient;
    }

    public void setUtilisateur(String utilisateur)
    {
        getClient().setLogin(utilisateur);
    }

    public void setPwd(String pwd)
    {
        getClient().setPwd(pwd);
    }

    public void setLoginValide(boolean val)
    {
        _loginValide = val;
    }


    /********************************/
    /*            Methodes          */
    /********************************/

    private void initComponents()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /********************************/
    /*            Boutons           */
    /********************************/
    private void onOK()
    {
        setUtilisateur(loginField.getText());
        setPwd(passwordField.getText());

        ReponseTRAMAP rep = getClient().sendLogin();

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            labelError.setText("");
            setLoginValide(true);
            dispose();
        }
        else
        {
            labelError.setText(rep.getMessage() + " !");
        }
    }

    private void onCancel()
    {
        setLoginValide(false);
        dispose();
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main(String[] args)
    {
        DialogLogin dialog = new DialogLogin();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
