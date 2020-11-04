//Auteurs : HENDRICK Samuel et DELAVAL Kevin
//Groupe : 2302
//Projet : R.T.I.
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import protocol.PIDEP.DonneeGetGrCouleurComp;
import protocol.PIDEP.GrCouleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class DialogGrCouleurComp extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;

    private Vector _trim1;
    private Vector _trim2;
    private Vector _trim3;
    private Vector _trim4;

    private int annee;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogGrCouleurComp()
    {
        setContentPane(contentPane);
        setModal(true);

        initComponants();
    }

    public DialogGrCouleurComp(java.awt.Frame parent, boolean modal, DonneeGetGrCouleurComp donnee)
    {
        super(parent, modal);
        setContentPane(contentPane);

        _trim1 = donnee.getTrim1();
        _trim2 = donnee.getTrim2();
        _trim3 = donnee.getTrim3();
        _trim4 = donnee.getTrim4();

        annee = donnee.get_annee();

        showHistogram();
        initComponants();
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponants()
    {
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

    private void showHistogram()
    {

       /* String[] trimestres = {"1er trimestre", "2ème trismestre", "3ème trimestre", "4ème trimestre"};

        DefaultCategoryDataset ds = new DefaultCategoryDataset();*/


        String[] trimestres = {"1er trimestre", "2ème trismestre", "3ème trimestre", "4ème trimestre"};


        DefaultCategoryDataset ds = new DefaultCategoryDataset();


        if(_trim1 != null)
        {
            for(int i = 0 ; i < _trim1.size() ; i++)
            {
                ds.addValue(((GrCouleur)_trim1.get(i)).get_nombre(), ((GrCouleur)_trim1.get(i)).get_destination(), trimestres[0]);
            }
        }

        if(_trim2 != null)
        {
            for (int i = 0; i < _trim2.size(); i++)
            {
                ds.addValue(((GrCouleur) _trim2.get(i)).get_nombre(), ((GrCouleur) _trim2.get(i)).get_destination(), trimestres[1]);
            }
        }

        if(_trim3 != null)
        {
            for (int i = 0; i < _trim3.size(); i++)
            {
                ds.addValue(((GrCouleur) _trim3.get(i)).get_nombre(), ((GrCouleur) _trim3.get(i)).get_destination(), trimestres[2]);
            }
        }

        if(_trim3 != null)
        {
            for (int i = 0; i < _trim4.size(); i++)
            {
                ds.addValue(((GrCouleur) _trim4.get(i)).get_nombre(), ((GrCouleur) _trim4.get(i)).get_destination(), trimestres[3]);
            }
        }


        JFreeChart jfc = ChartFactory.createBarChart("Répartition du nombre de containers en fonction de la destination par trimestre : année "+annee, "Trimestres", "Nombre de containers", ds, PlotOrientation.VERTICAL, true, true, false);

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
        DialogGrCouleurComp dialog = new DialogGrCouleurComp();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
