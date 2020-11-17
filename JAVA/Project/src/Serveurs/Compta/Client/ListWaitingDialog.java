package Serveurs.Compta.Client;

import protocol.BISAMAP.DonneeListWaiting;
import protocol.BISAMAP.Facture;
import protocol.BISAMAP.ReponseBISAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ListWaitingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private ClientCompta _client;

    public ListWaitingDialog(ClientCompta client) {
        _client = client;
        setContentPane(contentPane);
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
        ReponseBISAMAP bisamap;
        try{
            if(textField1.getText().equals(""))
                bisamap = _client.ListWaiting(_client.get_login());
            else
                bisamap = _client.ListWaiting(_client.get_login(), textField1.getText());

            if(bisamap.getCode() == 200)
            {
                List<Facture> factures = ((DonneeListWaiting)bisamap.getChargeUtile()).get_facturesDecryptees();
                //todo: afficher
            }
            else
            {
                //todo: afficher message
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
