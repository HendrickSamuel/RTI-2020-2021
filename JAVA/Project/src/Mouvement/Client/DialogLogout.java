package Mouvement.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.TRAMAP.DonneeLogout;
import protocol.TRAMAP.ReponseTRAMAP;
import protocol.TRAMAP.RequeteTRAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DialogLogout extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel labelText;

    private Client _client;
    private boolean _logout;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogLogout()
    {
        setModal(true);

        initComponents();
    }

    public DialogLogout(java.awt.Frame parent, boolean modal, Client client)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);

        _client = client;
        setLogout(false);

        labelText.setText("Bonjour "+ _client.getLogin() + " etes vous sur de vouloir vous deconnecter ?");
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public boolean isLogout()
    {
        return _logout;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setLogout(boolean log)
    {
        _logout = log;
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
        setLogout(true);
        dispose();
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
        DialogLogout dialog = new DialogLogout();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
