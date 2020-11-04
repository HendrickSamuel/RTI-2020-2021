//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import protocol.PIDEP.GrCouleur;
import protocol.PIDEP.DonneeGetGrCouleurRep;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class DialogGrCouleurRep extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel chartPanel;
    private JPanel buttonPanel;

    private Vector _data;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogGrCouleurRep()
    {
        setContentPane(contentPane);
        setModal(true);

        initComponants();
    }

    public DialogGrCouleurRep(java.awt.Frame parent, boolean modal, DonneeGetGrCouleurRep donnee)
    {
        super(parent, modal);
        setContentPane(contentPane);

        _data = donnee.get_retour();

        showPieChart();
        initComponants();
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

    private void onOK()
    {
        dispose();
    }

    private void onCancel()
    {
        dispose();
    }

    private void showPieChart()
    {
        //Définir un dataset qui contient les data
        DefaultPieDataset ds = new DefaultPieDataset();

        for(int i = 0 ; i < _data.size() ; i++)
        {
            ds.setValue(((GrCouleur)_data.get(i)).get_destination() , ((GrCouleur)_data.get(i)).get_nombre());
        }

        //Se fournir un JFreeChart
        JFreeChart jfc = ChartFactory.createPieChart ("Résulats du nombre de containers en fonction de la destination", ds, true,  true, true);

        //Fabriquer le Panel
        ChartPanel cp = new ChartPanel(jfc);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(cp);
        content.add(buttonOK, BorderLayout.SOUTH);
        setContentPane(content);
    }


    /********************************/
    /*              Main            */
    /********************************/
    public static void main(String[] args)
    {
        DialogGrCouleurRep dialog = new DialogGrCouleurRep();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
