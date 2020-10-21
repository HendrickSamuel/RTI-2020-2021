//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 13/10/2020

package Mouvement.Client;

import genericRequest.DonneeRequete;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DialogGetList extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonRetour;
    private JRadioButton rechercheParSociétéRadioButton;
    private JRadioButton rechercheParDestinationRadioButton;
    private JTextField rechercheField;
    private JTable table;
    private JPanel datePane;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;

    UtilDateModel model;
    JDatePanelImpl datePanel;
    JDatePickerImpl datePicker;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogGetList()
    {
        initComponents();
        setModal(true);


       /* model = new UtilDateModel();
        datePanel = new JDatePanelImpl();
        datePicker = new JDatePickerImpl();

        datePane.add(datePicker);*/
    }

    public DialogGetList(java.awt.Frame parent, boolean modal, String nam, String pass)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);

        dt = null;
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
        DialogGetList dialog = new DialogGetList();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
