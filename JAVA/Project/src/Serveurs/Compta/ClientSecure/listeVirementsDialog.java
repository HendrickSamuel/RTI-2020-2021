package Serveurs.Compta.ClientSecure;

import protocol.SAMOP.Virement;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class listeVirementsDialog extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private List<Virement> listDepart;
    private DefaultListModel _virements;
    private List<Virement> listRetour;

    private JPanel contentPane;
    private JButton buttonOK;
    private JList listVirements;
    private JButton validerLaSelectionButton;
    private JButton validerLaTotaliteButton;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public listeVirementsDialog()
    {
        initComponents();
    }

    public listeVirementsDialog(List<Virement> list)
    {
        initComponents();

        set_virements(new DefaultListModel());

        listDepart = list;

        for(Virement virement : list)
        {
            get_virements().addElement(virement);
        }

        listVirements.setModel(get_virements());
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public DefaultListModel get_virements()
    {
        return _virements;
    }

    public List<Virement> getListRetour()
    {
        return listRetour;
    }

    public JButton getValiderLaSelectionButton()
    {
        return validerLaSelectionButton;
    }

    public JButton getValiderLaTotaliteButton()
    {
        return validerLaTotaliteButton;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_virements(DefaultListModel _virements)
    {
        this._virements = _virements;
    }

    public void setListRetour(List<Virement> listRetour)
    {
        this.listRetour = listRetour;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
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

        validerLaSelectionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onValid();
            }
        });

        validerLaTotaliteButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onValidAll();
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

    private void onValid()
    {
        setListRetour(listVirements.getSelectedValuesList());
        dispose();
    }

    private void onValidAll()
    {
        setListRetour(listDepart);
        dispose();
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
    /*             Main             */
    /********************************/
    public static void main(String[] args)
    {
        listeVirementsDialog dialog = new listeVirementsDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
