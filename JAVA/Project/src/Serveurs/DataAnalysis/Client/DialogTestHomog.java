//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;


import protocol.PIDEP.DonneeGetStatInferTestHomog;
import javax.swing.*;
import java.awt.event.*;

public class DialogTestHomog extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel titreLabel;
    private JLabel detailLabel;
    private JLabel varLabel;
    private JLabel pvalueLabel;
    private JLabel hLabel;
    private JLabel desisionLabel;
    private JLabel welchLabel;

    private DonneeGetStatInferTestHomog donnee;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogTestHomog()
    {
        setContentPane(contentPane);
        setModal(true);

        initComponants();
    }

    public DialogTestHomog(java.awt.Frame parent, boolean modal, DonneeGetStatInferTestHomog don)
    {
        super(parent, modal);
        setContentPane(contentPane);

        initComponants();

        donnee = don;

        affichResult();
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

    private void affichResult()
    {
        titreLabel.setText("Test d'hypothèse d'homogénéité");
        detailLabel.setText("Taille de l'échantillon : " + donnee.get_tailleEch());

        if(donnee.getVarMin() < donnee.getResultVariance() && donnee.getVarMax() > donnee.getResultVariance())
        {
            varLabel.setText("La valeur "+ donnee.getResultVariance() +" est dans les bornes ["+ donnee.getVarMin() +" - "+ donnee.getVarMax() +"]");
            welchLabel.setText("le test homogénéité de moyenne se fera sans le correctif de Welch");
        }
        else
        {
            varLabel.setText("La valeur "+ donnee.getResultVariance() +" est hors des bornes ["+ donnee.getVarMin() +" - "+ donnee.getVarMax() +"]");
            welchLabel.setText("le test homogénéité de moyenne se fera sans le avec de Welch");
        }

        if(donnee.getP_value() < 0.05)
        {
            desisionLabel.setText("Différence significative, donc peu de chances de se tromper en rejetant H0");
        }
        else
        {
            desisionLabel.setText("Différence non significative, donc peu de chances de se tromper en gardant H0");
        }
        pvalueLabel.setText(String.valueOf(donnee.getP_value()));
        hLabel.setText("H0 : le temps moyen de stationnement d'un container est-il le même si il est à destination de Duisbourg ou Strasbourg");
    }

    /********************************/
    /*              Main            */
    /********************************/
    public static void main(String[] args)
    {
        DialogTestHomog dialog = new DialogTestHomog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
