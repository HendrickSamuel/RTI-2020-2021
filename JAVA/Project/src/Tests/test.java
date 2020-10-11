package Tests;

import MyGenericServer.ListeTaches;
import MyGenericServer.ThreadServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test extends JFrame {
    private JSlider slider1;
    private JButton button1;
    private JPanel test;
    private JTree tree1;

    public test() {
        super("Titre marrant");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.test);
        this.pack();

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
                int port = Integer.parseInt(mp.getContent("PORT"));
                System.out.println(port);
                ListeTaches lt = new ListeTaches();
                ThreadServer ts = new ThreadServer(port, lt, null, true,"protocolTRAMAP.TraitementTRAMAP");
                ts.start();
            }
        });


    }

    public static void main (String[] args){
        JFrame frame = new test();
        frame.setVisible(true);
    }
}
