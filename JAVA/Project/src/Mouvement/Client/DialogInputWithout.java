//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Mouvement.Client;

import javax.swing.*;
import java.awt.event.*;

public class DialogInputWithout extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField containerField;
    private JTextField immatriculationField;
    private JLabel labelRetour;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogInputWithout()
    {
        setModal(true);

        initComponents();
    }

    public DialogInputWithout(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getContainer()
    {
        return containerField.getText();
    }

    public String getImmatriculation()
    {
        return immatriculationField.getText();
    }

    
    /********************************/
    /*            Setters           */
    /********************************/
    public void setMessage(String retour)
    {
        labelRetour.setText(retour);
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private void onOK()
    {
        // add your code here
        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
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

        buttonCancel.addActionListener(new ActionListener()
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
