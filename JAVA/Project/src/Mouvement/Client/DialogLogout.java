package Mouvement.Client;

import genericRequest.DonneeRequete;
import genericRequest.MyProperties;
import protocol.TRAMAP.DonneeLogout;
import protocol.TRAMAP.ReponseTRAMAP;
import protocol.TRAMAP.RequeteTRAMAP;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DialogLogout extends JDialog
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel labelText;

    private Client _client;
    private boolean _logout;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private DonneeRequete dt;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DialogLogout()
    {
        setModal(true);

        initComponents();
    }

    public DialogLogout(java.awt.Frame parent, boolean modal, Client client)
    {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);

        dt = null;

        _client = client;
        setLogout(false);

        labelText.setText("Bonjour "+ _client.getLogin() + " etes vous sur de vouloir vous deconnecter ?");
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public boolean isLogout()
    {
        return _logout;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setLogout(boolean log)
    {
        _logout = log;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
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
        dt = new DonneeLogout(_client.getLogin(), _client.getPwd());
        RequeteTRAMAP req = null;
        req = new RequeteTRAMAP(dt);

        MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
        String HOST = mp.getContent("IPSERV");
        int PORT = Integer.parseInt(mp.getContent("PORT1"));

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket(HOST, PORT);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");//TODO:quitter
            dispose();
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");//TODO:quitter
            dispose();
        }

        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            //pour vider le cache
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");//TODO:quitter
            dispose();
        }
        // Lecture de la réponse
        ReponseTRAMAP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseTRAMAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
            }
            if(rep.getCode() == 201)
            {
                setLogout(true);
                dispose();
            }
            else
            {
                //todo: ???
            }

        }
        catch (ClassNotFoundException e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
            dispose();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            dispose();
        }
        setLogout(true);
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
        DialogLogout dialog = new DialogLogout();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
