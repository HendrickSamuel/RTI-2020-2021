//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;


import protocol.PIDEP.DonneeGetStatInferTestConf;
import javax.swing.*;
import java.awt.event.*;

public class DialogTestConf extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel titreLabel;
    private JLabel detailLabel;
    private JLabel p_valueLabel;
    private JLabel DecisionLabel;
    private JLabel labelH;

    private int ech;
    private double p_value;



    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogTestConf()
    {
        setContentPane(contentPane);
        setModal(true);

        initComponants();
    }

    public DialogTestConf(java.awt.Frame parent, boolean modal, DonneeGetStatInferTestConf donnee)
    {
        super(parent, modal);
        setContentPane(contentPane);

        initComponants();

        ech = donnee.get_taillEch();
        p_value = donnee.getP_value();

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
        titreLabel.setText("Test d'hypothèse de conformité");
        detailLabel.setText("Taille de l'échantillon : " + ech);
        p_valueLabel.setText(String.valueOf(p_value));

        if(p_value < 0.05)
        {
            DecisionLabel.setText("Différence significative, donc peu de chances de se tromper en rejetant H0");
        }
        else
        {
            DecisionLabel.setText("Différence non significative, donc peu de chances de se tromper en gardant H0");
        }

        labelH.setText("H0 : le temps moyen de stationnement d'un container est supposé être de 10 jours");
    }

    /********************************/
    /*              Main            */
    /********************************/
    public static void main(String[] args)
    {
        DialogTestConf dialog = new DialogTestConf();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
