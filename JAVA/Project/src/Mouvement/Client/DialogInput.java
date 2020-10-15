//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Mouvement.Client;

import javax.swing.*;
import java.awt.event.*;

public class DialogInput extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
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

    public DialogInput(java.awt.Frame parent, boolean modal)
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
