//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;




import protocol.PIDEP.ReponsePIDEP;
import javax.swing.*;


public class ClientAnalysisLayout extends JFrame
{
    /********************************/
    /*           Variables          */
    /********************************/
    private ClientAnalysis _client;

    private JPanel mainPanel;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton LoginOKButton;
    private JButton LoginCancelButton;
    private JPanel LoginPanel;
    private JPanel AppPanel;
    private JLabel LoginErrorLabel;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ClientAnalysisLayout()
    {
        super("Client Serveurs.Data analysis");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        setClient(new ClientAnalysis());
        setConnect(false);

        initComponents();
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public ClientAnalysis getClient()
    {
        return _client;
    }

    public boolean isConnect()
    {
        return _client.isConnect();
    }

    public String getUtilisateur()
    {
        return getClient().getLogin();
    }

    public String getPwd()
    {
        return getClient().getPwd();
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setClient(ClientAnalysis tmpClient)
    {
        _client = tmpClient;
    }

    public void setConnect(boolean tmpConnect)
    {
        _client.setConnect(tmpConnect);
    }

    public void setUtilisateur(String utilisateur)
    {
        getClient().setLogin(utilisateur);
    }

    public void setPwd(String pwd)
    {
        getClient().setPwd(pwd);
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    private void initComponents()
    {
        AppPanel.setVisible(false);

        LoginOKButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLoginOK(evt);
            }
        });

        LoginCancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onLoginCancel(evt);
            }
        });
    }

    private void onLoginOK(java.awt.event.ActionEvent evt)
    {
        //todo
        setUtilisateur(userField.getText());
        setPwd(String.valueOf(passwordField.getPassword()));

        ReponsePIDEP rep = getClient().sendLogin();

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            LoginPanel.setVisible(false);
            AppPanel.setVisible(true);
        }
        else
        {
            LoginErrorLabel.setText("Nom d'utilisateur ou mot de passe incorrecte");
        }
    }

    private void onLoginCancel(java.awt.event.ActionEvent evt)
    {
        userField.setText("");
        passwordField.setText("");
    }

    /********************************/
    /*             Main             */
    /********************************/
    public static void main (String[] args)
    {
        JFrame frame = new ClientAnalysisLayout();
        frame.setVisible(true);
    }
}