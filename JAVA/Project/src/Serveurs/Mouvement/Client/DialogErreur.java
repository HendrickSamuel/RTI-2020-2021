//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Client;

import javax.swing.*;
import java.awt.event.*;

public class DialogErreur extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String _message;
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelMassage;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogErreur()
    {
        setModal(true);

        initComponents();
    }

    public DialogErreur(java.awt.Frame parent, boolean modal, String message)
    {
        super(parent, modal);
        initComponents();

        this.setTitle("Erreur Serveurs.Mouvement");
        this.setLocationRelativeTo(null);

        setMessage(message);

        setText();
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getMessage()
    {
        return _message;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setMessage(String message)
    {
        _message = message;
    }

    public void setText()
    {
        labelMassage.setText(getMessage());
    }

    /********************************/
    /*            Methodes          */
    /********************************/
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
    }

    /********************************/
    /*            Boutons           */
    /********************************/
    private void onOK()
    {
        dispose();
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main(String[] args)
    {
        DialogErreur dialog = new DialogErreur();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
