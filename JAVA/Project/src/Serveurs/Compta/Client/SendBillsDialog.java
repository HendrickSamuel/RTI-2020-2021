package Serveurs.Compta.Client;

import genericRequest.Reponse;
import protocol.BISAMAP.DonneeListBills;
import protocol.BISAMAP.Facture;
import protocol.BISAMAP.ReponseBISAMAP;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class SendBillsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JTextField textSociete;
    private JTextField textDepart;
    private JTextField textFin;
    private JButton GETBILLSButton;
    private JScrollPane scrollPane;
    private ClientCompta _client;

    public SendBillsDialog(ClientCompta client) {
        table1.setModel(new DefaultTableModel(
                null,
                new String [] {"Id", "Societe", "Mois", "Annee", "TVA", "NE PAS ENVOYER"}
        ));
        table1.setFillsViewportHeight(true);

        super.setTitle("Liste des factures");
        _client = client;
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(350,700);
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

        GETBILLSButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGetBills();
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

    private void onGetBills()
    {
        table1.setModel(new DefaultTableModel(
                null,
                new String [] {"Id", "Societe", "Mois", "Annee", "TVA", "NE PAS ENVOYER"}
        ));
        table1.setFillsViewportHeight(true);
        try{
            Date dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(textDepart.getText());
            Date dateFin = new SimpleDateFormat("dd/MM/yyyy").parse(textFin.getText());

            ReponseBISAMAP bisamap = _client.ListBills(textSociete.getText(), dateDebut, dateFin);
            if(bisamap.getCode() == 200)
            {
                List<Facture> factures = ((DonneeListBills)bisamap.getChargeUtile()).getFacturesDecryptees();
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
                    ligne.add("0");

                    dtm.addRow(ligne);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, bisamap.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void onOK() {
        try {
            ReponseBISAMAP bisamap = _client.SendBills(_client.get_login(), null);
            if(bisamap.getCode() == 200)
            {
                JOptionPane.showMessageDialog(null, "Factures envoyées");
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
