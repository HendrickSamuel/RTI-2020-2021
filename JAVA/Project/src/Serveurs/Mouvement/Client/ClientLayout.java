//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Client;


import javax.swing.*;


public class ClientLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Client _client;

    private JPanel mainPanel;
    private JButton buttonLogin;
    private JButton buttonLogout;
    private JButton buttonInputLorry;
    private JButton buttonOperations;
    private JButton buttonInputLorryWithoutRes;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientLayout()
    {
        super("Client Serveurs.Mouvement");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        initComponents();

        setClient(new Client());
        setConnect(false);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public Client getClient()
    {
        return _client;
    }

    public boolean isConnect()
    {
        return _client.isConnect();
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setClient(Client tmpClient)
    {
        _client = tmpClient;
    }

    public void setConnect(boolean tmpConnect)
    {
        _client.setConnect(tmpConnect);
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        buttonInputLorry.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onInputLorry(evt);
            }
        });

        buttonInputLorryWithoutRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onInputLorryWithoutRes(evt);
            }
        });

        buttonOperations.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onOperations(evt);
            }
        });

        buttonLogout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLogout(evt);
            }
        });

        buttonLogin.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLogin(evt);
            }
        });
    }

    private void onInputLorry(java.awt.event.ActionEvent evt)
    {
        this.setVisible(false);

        if(isConnect())
        {
            DialogInput input = new DialogInput(this, true, getClient());
            input.setSize(450, 250);
            input.setVisible(true);
        }
        else
        {
            showError("Vous n'etes pas connecte");
        }

        this.setVisible(true);
    }

    private void onInputLorryWithoutRes(java.awt.event.ActionEvent evt)
    {
        this.setVisible(false);

        if(isConnect())
        {
            DialogInputWithout input = new DialogInputWithout(this, true, getClient());
            input.setSize(450, 250);
            input.setVisible(true);
        }
        else
        {
            showError("Vous n'etes pas connecte");
        }

        this.setVisible(true);
    }

    private void onOperations(java.awt.event.ActionEvent evt)
    {
        this.setVisible(false);

        if(isConnect())
        {
            DialogGetList getList = new DialogGetList(this, true, getClient());
            getList.setSize(500, 500);
            getList.setVisible(true);
        }
        else
        {
            showError("Vous n'etes pas connecte");
        }

        this.setVisible(true);
    }

    private void onLogout(java.awt.event.ActionEvent evt)
    {
        this.setVisible(false);

        if(isConnect())
        {
            DialogLogout logout = new DialogLogout(this, true, getClient());
            logout.setSize(260, 190);
            logout.setVisible(true);
            if(logout.isLogout())
            {
                setConnect(false);
            }
        }
        else
        {
            showError("Vous n'etes pas connecte");
        }

        this.setVisible(true);
    }

    private void onLogin(java.awt.event.ActionEvent evt)
    {
        this.setVisible(false);

        if(isConnect())
        {
            showError("Vous etes deja connecte");
        }
        else
        {
            DialogLogin login = new DialogLogin(this, true, getClient());
            login.setSize(320, 210);
            login.setVisible(true);

            if(login.getLoginValide())
            {
                setConnect(true);
            }
        }

        this.setVisible(true);
    }

    private void showError(String message)
    {
        DialogErreur erreur = new DialogErreur(this, true, message);
        erreur.setSize(260, 190);
        erreur.setVisible(true);
    }

    /********************************/
    /*             Main             */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = new ClientLayout();
        frame.setVisible(true);
    }

}
