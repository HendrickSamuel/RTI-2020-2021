package Serveurs.Compta.ClientSecure;

import javax.swing.*;
import java.awt.event.*;

public class messageDialog extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel messageLabel;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public messageDialog()
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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
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

    public messageDialog(String message)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        messageLabel.setText(message);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
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

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/
    private void onOK()
    {
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
        messageDialog dialog = new messageDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
