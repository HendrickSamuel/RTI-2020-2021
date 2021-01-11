package Serveurs.Compta.Client;

import protocol.BISAMAP.DonneeListWaiting;
import protocol.BISAMAP.Facture;
import protocol.BISAMAP.ReponseBISAMAP;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ListWaitingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTable table1;
    private ClientCompta _client;

    public ListWaitingDialog(ClientCompta client) {
        _client = client;
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(700,350);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        table1.setModel(new DefaultTableModel(
                null,
                new String [] {"Id", "Societe", "Mois", "Annee", "TVA"}
        ));
        table1.setFillsViewportHeight(true);

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
        table1.setModel(new DefaultTableModel(
                null,
                new String [] {"Id", "Societe", "Mois", "Annee", "TVA"}
        ));

        ReponseBISAMAP bisamap;
        try{
            if(textField1.getText().equals(""))
                bisamap = _client.ListWaiting(_client.get_login());
            else
                bisamap = _client.ListWaiting(_client.get_login(), textField1.getText());

            if(bisamap.getCode() == 200)
            {
                List<Facture> factures = ((DonneeListWaiting)bisamap.getChargeUtile()).get_facturesDecryptees();
                Vector ligne;
                DefaultTableModel dtm = (DefaultTableModel) table1.getModel();

                for(Facture facture: factures)
                {
                    ligne = new Vector();
                    ligne.add(facture.get_id());
                    ligne.add(facture.get_societe());
                    ligne.add(facture.get_annee());
                    ligne.add(facture.get_mois());
                    ligne.add(facture.get_tva());

                    dtm.addRow(ligne);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, bisamap.getMessage());
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
