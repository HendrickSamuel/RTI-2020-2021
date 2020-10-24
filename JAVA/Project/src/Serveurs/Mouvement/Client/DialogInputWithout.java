//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Client;

import genericRequest.DonneeRequete;
import protocol.TRAMAP.DonneeInputLoryWithoutReservation;
import protocol.TRAMAP.ReponseTRAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DialogInputWithout extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Client _client;

    private JLabel labelRetour;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonRetour;
    private JTextField societeField;
    private JTextField containerField;
    private JTextField destinationField;
    private JTextField immatriculationField;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogInputWithout()
    {
        setModal(true);

        initComponents();
    }

    public DialogInputWithout(java.awt.Frame parent, boolean modal, Client client)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);

        setClient(client);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public Client getClient()
    {
        return _client;
    }

    public String getContainer()
    {
        return containerField.getText();
    }

    public String getImmatriculation()
    {
        return immatriculationField.getText();
    }

    public String getSociete()
    {
        return societeField.getText();
    }

    public String getDestination()
    {
        return destinationField.getText();
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setClient(Client tmpClient)
    {
        _client = tmpClient;
    }

    public void setMessage(String retour)
    {
        labelRetour.setText(retour);
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private void onOK()
    {
        ReponseTRAMAP rep = getClient().sendInputLorryWithoutReservation(getContainer(),getImmatriculation(), getSociete(), getDestination());

        if(rep == null)
        {
            labelRetour.setText("Problème de connexion avec le serveur");
        }
        else
        {
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
            }
            if(rep.getCode() == 200)
            {
                DonneeInputLoryWithoutReservation donnee =(DonneeInputLoryWithoutReservation) rep.getChargeUtile();
                labelRetour.setText("Vous pouvez mettre le container en place [" + donnee.getX() +"] [" + donnee.getY() + "]");
            }
            else
            {
                labelRetour.setText(rep.getMessage());
            }

        }
    }

    private void onCancel()
    {
        dispose();
    }

    private void initComponents()
    {
        setContentPane(contentPane);
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

    /********************************/
    /*             Main             */
    /********************************/
    public static void main(String[] args)
    {
        DialogInputWithout dialog = new DialogInputWithout();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
