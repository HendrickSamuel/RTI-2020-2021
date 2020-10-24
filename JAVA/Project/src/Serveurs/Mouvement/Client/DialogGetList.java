//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Client;

import genericRequest.DonneeRequete;
import protocol.TRAMAP.ReponseTRAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogGetList extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Client _client;
    private String _dateDebut;
    private String _dateFin;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonRetour;
    private JRadioButton rechercheParSociétéRadioButton;
    private JRadioButton rechercheParDestinationRadioButton;
    private JTextField rechercheField;
    private JTable table;
    private JPanel datePane;
    private JTextField dateDebutField;
    private JTextField dateFinField;
    private JLabel labelRetour;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogGetList()
    {
        initComponents();
        setModal(true);
    }

    public DialogGetList(java.awt.Frame parent, boolean modal, Client client)
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

    public String getRecherche()
    {
        return rechercheField.getText();
    }

    public String getDateDebut()
    {
        _dateDebut = dateDebutField.getText();
        return _dateDebut;
    }

    public String getDateFin()
    {
        _dateFin = dateFinField.getText();
        return _dateFin;
    }

    /********************************/
    /*            Setters           */
    /********************************/
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
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonRetour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK()
    {
        ReponseTRAMAP rep = null;

        Date dateDebut = null;
        Date dateFin = null;

        try
        {
            dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(getDateDebut());
            dateFin = new SimpleDateFormat("dd/MM/yyyy").parse(getDateFin());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        if(rechercheParSociétéRadioButton.isSelected())
        {
            rep = getClient().sendGetList(dateDebut, dateFin, getRecherche(), null);
        }
        else
        {
            rep = getClient().sendGetList(dateDebut, dateFin, null, getRecherche());
        }

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
                //todo: decoder le message
                labelRetour.setText(rep.getMessage());
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
        DialogGetList dialog = new DialogGetList();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
