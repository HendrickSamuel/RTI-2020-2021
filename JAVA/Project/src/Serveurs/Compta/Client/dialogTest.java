package Serveurs.Compta.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.*;

public class dialogTest extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;

    private ClientCompta _client;

    public dialogTest(ClientCompta client) {
        _client = client;
        setContentPane(contentPane);
        this.setTitle("Login");
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(200,200);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
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

    private void onOK() {
        try {
            if(_client.SendLogin(textField1.getText(), new String(passwordField1.getPassword())))
            {
                dispose();
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException | NoSuchPaddingException | UnrecoverableKeyException | IllegalBlockSizeException | BadPaddingException | KeyStoreException | InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
