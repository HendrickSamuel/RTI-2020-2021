//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Mouvement.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.TRAMAP.DonneeInputLory;
import protocol.TRAMAP.ReponseTRAMAP;
import protocol.TRAMAP.RequeteTRAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DialogInput extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonRetour;
    private JTextField containerField;
    private JTextField reservationField;
    private JLabel labelRetour;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogInput()
    {
        setModal(true);

        initComponents();
    }

    public DialogInput(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);

        dt = null;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getContainer()
    {
        return containerField.getText();
    }

    public String getReservation()
    {
        return reservationField.getText();
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setRetour(String message)
    {
        labelRetour.setText(message);
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

        buttonRetour.addActionListener(new ActionListener()
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
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    private void onOK()
    {
        dt = new DonneeInputLory(getReservation(), getContainer());
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
                labelRetour.setText(rep.getMessage());
                //dispose();
            }
            else
            {
                labelRetour.setText(rep.getMessage());
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
        dispose();
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main(String[] args)
    {
        DialogInput dialog = new DialogInput();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
