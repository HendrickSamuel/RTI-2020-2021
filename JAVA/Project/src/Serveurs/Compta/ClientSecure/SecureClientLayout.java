//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package Serveurs.Compta.ClientSecure;

import protocol.SAMOP.*;

import javax.swing.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SecureClientLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private SecureClientCompta _client;

    private JPanel mainPanel;
    private JPanel loginPanel;
    private JLabel labelError;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton validLoginButton;
    private JPanel appPanel;
    private JRadioButton computeSalRadio;
    private JRadioButton validateSalRadio;
    private JRadioButton askPaymentsRadioButton;
    private JRadioButton launchPayementsRadio;
    private JButton validerButton;
    private JPanel askPaymentPanel;
    private JTextField monthField;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public SecureClientLayout() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
    {
        super("Client Secure Serveur Compta");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        set_client(new SecureClientCompta());
        setConnect(false);

        initComponents();
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public SecureClientCompta get_client()
    {
        return _client;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_client(SecureClientCompta _client)
    {
        this._client = _client;
    }

    public void setConnect(boolean tmpConnect)
    {
        get_client().set_connected(tmpConnect);
    }

    public void setUtilisateur(String utilisateur)
    {
        get_client().set_login(utilisateur);
    }

    public void setPwd(String pwd)
    {
        get_client().set_password(pwd);
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        appPanel.setVisible(false);
        askPaymentPanel.setVisible(false);

        validLoginButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onValidLogin(evt);
            }
        });

        validerButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onValid(evt);
            }
        });

        computeSalRadio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onComputeSal(evt);
            }
        });

        validateSalRadio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onValidateSal(evt);
            }
        });

        askPaymentsRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onAskPayments(evt);
            }
        });

        launchPayementsRadio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLaunchPayements(evt);
            }
        });
    }

    private void computeSal()
    {
        try
        {
            ReponseSAMOP rep = get_client().sendComputeSal();

            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
            }
            if(rep.getCode() == 200)
            {
                labelError.setText("");

                messageDialog dial = new messageDialog("Le calcul des salaires à bien été effectué");
                dial.setSize(300, 150);
                dial.setLocationRelativeTo(this);
                dial.setVisible(true);
            }
            else
            {
                labelError.setText(rep.getMessage());
            }
        }
        catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | SignatureException | NoSuchProviderException | InvalidKeyException e)
        {
            labelError.setText(e.getMessage());
        }
    }

    private void validateSal()
    {
        try
        {
            ReponseSAMOP rep = get_client().getValidateSal();

            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
            }
            if(rep.getCode() == 200)
            {
                labelError.setText("");

                listeVirementsDialog dial = new listeVirementsDialog(((DonneeValideSal)rep.getChargeUtile()).getListe());
                dial.setTitle("Virements en attente de validation");
                dial.setSize(400, 500);
                dial.setLocationRelativeTo(this);
                dial.setVisible(true);

                if(dial.getListRetour() != null && dial.getListRetour().size() > 0)
                {
                    rep = get_client().sendValidateSal(dial.getListRetour());

                    System.out.println(" *** Reponse reçue : " + rep.getCode());
                    if(rep.getMessage() != null)
                    {
                        System.out.println("Message reçu: " + rep.getMessage());
                    }
                    if(rep.getCode() == 200)
                    {
                        labelError.setText("");

                        messageDialog mes = new messageDialog("La validation des salaires à bien été effectuée");
                        mes.setSize(300, 150);
                        mes.setLocationRelativeTo(this);
                        mes.setVisible(true);
                    }
                    else
                    {
                        labelError.setText(rep.getMessage());
                    }
                }
            }
            else
            {
                labelError.setText(rep.getMessage());
            }
        }
        catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | SignatureException | NoSuchProviderException | InvalidKeyException e)
        {
            labelError.setText(e.getMessage());
        }
    }

    private void askPayments()
    {
        ReponseSAMOP rep = get_client().sendAskPayments(Integer.parseInt(monthField.getText()));

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            labelError.setText("");

            listeVirementsDialog dial = new listeVirementsDialog(((DonneeAskPayements)rep.getChargeUtile()).getListe());
            dial.setTitle("Virements effectué pour le mois demandé");
            dial.setSize(400, 500);
            dial.setLocationRelativeTo(this);
            dial.getValiderLaSelectionButton().setVisible(false);
            dial.getValiderLaTotaliteButton().setVisible(false);
            dial.setVisible(true);
        }
        else
        {
            labelError.setText(rep.getMessage());
        }
    }

    private void launchPayements()
    {
        ReponseSAMOP rep = get_client().getLaunchPayments();

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            labelError.setText("");

            listeVirementsDialog dial = new listeVirementsDialog(((DonneeLaunchPayements)rep.getChargeUtile()).getListe());
            dial.setTitle("Virements pouvant etre effectué");
            dial.setSize(400, 500);
            dial.setLocationRelativeTo(this);
            dial.setVisible(true);

            if(dial.getListRetour() != null && dial.getListRetour().size() > 0)
            {
                rep = get_client().sendLaunchPayments(dial.getListRetour());

                System.out.println(" *** Reponse reçue : " + rep.getCode());
                if(rep.getMessage() != null)
                {
                    System.out.println("Message reçu: " + rep.getMessage());
                }
                if(rep.getCode() == 200)
                {
                    labelError.setText("");

                    listeVirementsDialog dia = new listeVirementsDialog(((DonneeSendLauchPayements)rep.getChargeUtile()).getListe());
                    dia.setTitle("Virements venant d'etre effectué");
                    dia.setSize(400, 500);
                    dia.setLocationRelativeTo(this);
                    dia.getValiderLaSelectionButton().setVisible(false);
                    dia.getValiderLaTotaliteButton().setVisible(false);
                    dia.setVisible(true);
                }
                else
                {
                    labelError.setText(rep.getMessage());
                }
            }
        }
        else
        {
            labelError.setText(rep.getMessage());
        }
    }


    /********************************/
    /*             Events           */
    /********************************/
    private void onValidLogin(java.awt.event.ActionEvent evt)
    {
        setUtilisateur(usernameField.getText());
        setPwd(String.valueOf(passwordField.getText()));

        ReponseSAMOP rep = get_client().sendLogin();

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            labelError.setText("");

            loginPanel.setVisible(false);
            appPanel.setVisible(true);
        }
        else
        {
            labelError.setText(rep.getMessage());
        }
    }

    private void onValid(java.awt.event.ActionEvent evt)
    {
        if(computeSalRadio.isSelected())
        {
            computeSal();
        }
        else if(validateSalRadio.isSelected())
        {
            validateSal();
        }
        else if(askPaymentsRadioButton.isSelected())
        {
            askPayments();
        }
        else if(launchPayementsRadio.isSelected())
        {
            launchPayements();
        }
    }

    private void onComputeSal(java.awt.event.ActionEvent evt)
    {
        askPaymentPanel.setVisible(false);
    }

    private void onValidateSal(java.awt.event.ActionEvent evt)
    {
        askPaymentPanel.setVisible(false);
    }

    private void onAskPayments(java.awt.event.ActionEvent evt)
    {
        askPaymentPanel.setVisible(true);
    }

    private void onLaunchPayements(java.awt.event.ActionEvent evt)
    {
        askPaymentPanel.setVisible(false);
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = null;
        try
        {
            frame = new SecureClientLayout();
            frame.setSize(500, 300);
            frame.setVisible(true);
        }
        catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e)
        {
            e.printStackTrace();
        }
    }
}