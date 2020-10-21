//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Mouvement.Client;

import genericRequest.MyProperties;
import genericRequest.DonneeRequete;
import protocolTRAMAP.DonneeLogin;
import protocolTRAMAP.ReponseTRAMAP;
import protocolTRAMAP.RequeteTRAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DialogLogin extends javax.swing.JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private boolean _loginValide;
    private String _utilisateur;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;

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

        this.setTitle("Login Mouvement");
        this.setLocationRelativeTo(null);
        setLoginValide(false);
    }

    public DialogLogin(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);
        setLoginValide(false);

        dt = null;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getUtilisateur()
    {
        return _utilisateur;
    }

    public boolean getLoginValide()
    {
        return _loginValide;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setUtilisateur(String utilisateur)
    {
        _utilisateur = utilisateur;
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
        dt = new DonneeLogin(loginField.getText(),passwordField.getText());
        RequeteTRAMAP req = null;
        req = new RequeteTRAMAP(dt);

        MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
        String HOST = mp.getContent("IPSERV");
        int PORT = Integer.parseInt(mp.getContent("PORT1"));

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket(HOST, PORT);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");//TODO:quitter
            dispose();
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");//TODO:quitter
            dispose();
        }

        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            //pour vider le cache
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");//TODO:quitter
            dispose();
        }
        // Lecture de la réponse
        ReponseTRAMAP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseTRAMAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
            }
            if(rep.getCode() == 201)
            {
                labelError.setText("");
                setLoginValide(true);
                dispose();
            }
            else
            {
                labelError.setText("Login ou mot de passe incorrect !");
            }

        }
        catch (ClassNotFoundException e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
            dispose();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            dispose();
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
