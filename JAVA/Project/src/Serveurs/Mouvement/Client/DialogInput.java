//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Client;

import protocol.TRAMAP.DonneeInputLory;
import protocol.TRAMAP.ReponseTRAMAP;

import javax.swing.*;
import java.awt.event.*;

public class DialogInput extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Client _client;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonRetour;
    private JTextField containerField;
    private JTextField reservationField;
    private JLabel labelRetour;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogInput()
    {
        setModal(true);

        initComponents();
    }

    public DialogInput(java.awt.Frame parent, boolean modal, Client client)
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

    public void setClient(Client tmpClient)
    {
        _client = tmpClient;
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
        ReponseTRAMAP rep = getClient().sendInputLorry(getReservation(), getContainer());

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
                DonneeInputLory donnee =(DonneeInputLory) rep.getChargeUtile();
                labelRetour.setText("Vous pouvez mettre le container en place [" + donnee.getX() +"] [" + donnee.getY() + "]");
                buttonOK.setEnabled(false);
                reservationField.setEnabled(false);
                containerField.setEnabled(false);
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
