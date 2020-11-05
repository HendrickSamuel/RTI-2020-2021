//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;

import protocol.PIDEP.DonneeGetStatInferANOVA;
import javax.swing.*;
import java.awt.event.*;

public class DialogANOVA extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel titreLabel;
    private JLabel detailLabel;
    private JLabel pvalueLabel;
    private JLabel HLabel;
    private JLabel decisionLabel;

    private  DonneeGetStatInferANOVA donnee;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogANOVA()
    {
        setContentPane(contentPane);
        setModal(true);

        initComponants();
    }

    public DialogANOVA(java.awt.Frame parent, boolean modal, DonneeGetStatInferANOVA don)
    {
        super(parent, modal);
        setContentPane(contentPane);

        donnee = don;

        initComponants();
        afficheResult();
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponants()
    {
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
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
        dispose();
    }

    private void onCancel()
    {
        dispose();
    }

    private void afficheResult()
    {
        titreLabel.setText("Test d'hypothèse de type ANOVA");
        detailLabel.setText("Taille des l'échantillons : " + donnee.get_tailleEch());

        pvalueLabel.setText(String.valueOf(donnee.get_pvalue()));

        if(donnee.get_pvalue() < 0.05)
        {
            decisionLabel.setText("Différence significative, donc peu de chances de se tromper en rejetant H0");
        }
        else
        {
            decisionLabel.setText("Différence non significative, donc peu de chances de se tromper en gardant H0");
        }

        HLabel.setText("H0 : le temps moyen de stationnement d'un container est le même selon les différentes destinations possibles");
    }


    /********************************/
    /*              Main            */
    /********************************/
    public static void main(String[] args)
    {
        DialogANOVA dialog = new DialogANOVA();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
