package Serveurs.Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListCli extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JList list1;
    private DefaultListModel _clients;

    public DefaultListModel get_clients()
    {
        return _clients;
    }

    public void set_clients(DefaultListModel _clients)
    {
        this._clients = _clients;
    }

    public ListCli()
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
    }

    public ListCli(List<String> clients)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        set_clients(new DefaultListModel());

        for(String client : clients)
        {
            get_clients().addElement(client);
        }

        list1.setModel(get_clients());

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });
    }

    private void onOK()
    {
        // add your code here
        dispose();
    }

    public static void main(String[] args)
    {
        ListCli dialog = new ListCli();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
