//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 17/11/2020

package Serveurs.Compta.Client;

import Serveurs.Compta.Serveur.ServerLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class ClientLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel mainPanel;
    private JButton GETNEXTBILLButton;
    private JButton LISTBILLSButton;
    private JButton RECPAYButton;
    private JButton WAITINGBILLSButton;
    private ClientCompta _clientCompta;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientLayout() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        super("Client ClientCompta");
        _clientCompta = new ClientCompta();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setExtendedState(JFrame.MAXIMIZED_VERT);
        this.setContentPane(this.mainPanel);
        this.setMinimumSize(new Dimension(800,700));
        this.setSize(850,600);
        this.pack();
        this.setVisible(true);

        dialogTest dt = new dialogTest(_clientCompta);
        dt.setVisible(true);

        GETNEXTBILLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getNextBill(evt);
            }
        });

        LISTBILLSButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListBills(evt);
            }
        });

        RECPAYButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecPay(evt);
            }
        });

        WAITINGBILLSButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WaitBills(evt);
            }
        });
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = null;
        try {
            frame = new ClientLayout();
            frame.setVisible(true);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getNextBill(java.awt.event.ActionEvent evt)
    {
        NextBillDialog nnbd = new NextBillDialog(_clientCompta);
        nnbd.setVisible(true);
    }

    public void ListBills(java.awt.event.ActionEvent evt)
    {
        SendBillsDialog sbd = new SendBillsDialog(_clientCompta);
        sbd.setVisible(true);
    }

    public void RecPay(java.awt.event.ActionEvent evt)
    {
        RecPayDialog rp = new RecPayDialog(_clientCompta);
        rp.setVisible(true);
    }

    public void WaitBills(java.awt.event.ActionEvent evt)
    {
        ListWaitingDialog lwp = new ListWaitingDialog(_clientCompta);
        lwp.setVisible(true);
    }
}
