package Serveurs.Compta.Client;

import protocol.BISAMAP.DonneeGetNextBill;
import protocol.BISAMAP.Facture;
import protocol.BISAMAP.ReponseBISAMAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NextBillDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel textID;
    private JLabel textSociete;
    private JLabel textMois;
    private JLabel textAnnee;
    private JLabel textTVA;

    private ClientCompta _client;
    private Facture _factureEnCours;

    public NextBillDialog(ClientCompta client) {
        this.setTitle("Prochaine facture");
        _client = client;
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(350,200);
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

        try {
            ReponseBISAMAP bisamap = _client.GetNextBill();
            if(bisamap.getCode() == 200)
            {
                _factureEnCours = ((DonneeGetNextBill)bisamap.getChargeUtile()).get_facture();
                textID.setText(_factureEnCours.get_id()+"");
                textSociete.setText(_factureEnCours.get_societe());
                textMois.setText(_factureEnCours.get_mois()+"");
                textAnnee.setText(_factureEnCours.get_annee()+"");
                textTVA.setText(_factureEnCours.get_tva()+"");
                buttonOK.setEnabled(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, bisamap.getMessage());
                buttonOK.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onOK() {
        try {
            ReponseBISAMAP bisamap = _client.ValidateBill(_factureEnCours.get_id());
            if(bisamap.getCode() == 200)
            {
                JOptionPane.showMessageDialog(null, "Facture validee");
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
