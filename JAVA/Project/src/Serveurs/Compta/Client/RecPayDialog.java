package Serveurs.Compta.Client;

import protocol.BISAMAP.ReponseBISAMAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecPayDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFacture;
    private JTextField textMontant;
    private JTextField textPayement;
    private ClientCompta _client;

    public RecPayDialog(ClientCompta client) {
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(700,350);

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
        try {
        int facture = Integer.parseInt(textFacture.getText());
        float montant = Float.parseFloat(textMontant.getText());
            ReponseBISAMAP bisamap = _client.RecPay(facture, montant, _client.get_login(), textPayement.getText());
            if(bisamap.getCode() == 200)
            {
                JOptionPane.showMessageDialog(null, "Payement enregistré");
                this.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(null, bisamap.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
