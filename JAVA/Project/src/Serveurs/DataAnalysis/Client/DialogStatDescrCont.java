package Serveurs.DataAnalysis.Client;

import Serveurs.Mouvement.Client.Client;
import protocol.PIDEP.DonneeGetStatDescrCont;

import javax.swing.*;
import java.awt.event.*;

public class DialogStatDescrCont extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel textEcartType;
    private JLabel textMediane;
    private JLabel textMode;
    private JLabel textMoyenne;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogStatDescrCont()
    {
        setContentPane(contentPane);
        setModal(true);

        initComponants();
    }

    public DialogStatDescrCont(java.awt.Frame parent, boolean modal, DonneeGetStatDescrCont donnee)
    {
        super(parent, modal);
        setContentPane(contentPane);

        initComponants();

        textMoyenne.setText(String.valueOf(donnee.get_moyenne()));
        textEcartType.setText(String.valueOf(donnee.get_ecartType()));
        textMediane.setText(String.valueOf(donnee.get_mediane()));
        textMode.setText(String.valueOf(donnee.get_mode()));
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponants()
    {
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
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
        dispose();
    }

    private void onCancel()
    {
        dispose();
    }

    /********************************/
    /*              Main            */
    /********************************/
    public static void main(String[] args)
    {
        DialogStatDescrCont dialog = new DialogStatDescrCont();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
