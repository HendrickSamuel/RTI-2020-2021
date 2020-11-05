//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Client;


import protocol.PIDEP.*;
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
    private JLabel AppLabelError;
    private JRadioButton statDescContRadioButton;
    private JRadioButton histCompareRadioButton;
    private JRadioButton diagSectorielRadioButton;
    private JRadioButton statInferTestConfRadioButton;
    private JRadioButton statInferTestHomogRadioButton;
    private JRadioButton statInferANOVARadioButton;
    private JPanel PanelStatDescrCont;
    private JPanel PanelGrCouleurRep;
    private JPanel PanelGrCouleurComp;
    private JPanel PanelStatInferTestConf;
    private JPanel PanelStatInferTestHomog;
    private JPanel PanelStatInferANOVA;
    private JButton AppOKButton;
    private JTextField StatDescContField;
    private JRadioButton enEntréeRadioButton;
    private JRadioButton enSortieRadioButton;
    private JTextField CouleurCompAnneeField;
    private JTextField CouleurRepValeurField;
    private JRadioButton lAnneeRadioButton;
    private JRadioButton leMoisRadioButton;
    private JTextField StatInferConfField;
    private JTextField StatInferHomogField;
    private JTextField StatInferANOVAField;
    private JButton AppLogoutButton;


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
    private void statDescCont()
    {
        boolean entree;
        if(enEntréeRadioButton.isSelected())
        {
            entree = true;
        }
        else
        {
            entree = false;
        }

        ReponsePIDEP rep = getClient().sendGetStatDescrCont(Integer.parseInt(StatDescContField.getText()), entree);

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            DialogStatDescrCont dial = new DialogStatDescrCont(this, false, (DonneeGetStatDescrCont)rep.getChargeUtile());
            dial.setSize(300,200);
            dial.setVisible(true);
        }
        else
        {
            AppLabelError.setText(rep.getMessage());
        }
    }

    private void diagSectoriel()
    {
        boolean annee;
        if(lAnneeRadioButton.isSelected())
        {
            annee = true;
        }
        else
        {
            annee = false;
        }

        ReponsePIDEP rep = getClient().sendGrCouleurRep(Integer.parseInt(CouleurRepValeurField.getText()), annee);

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            DialogGrCouleurRep dial = new DialogGrCouleurRep(this, false, (DonneeGetGrCouleurRep)rep.getChargeUtile());
            dial.setSize(600,500);
            dial.setVisible(true);
        }
        else
        {
            AppLabelError.setText(rep.getMessage());
        }
    }

    private void histCompare()
    {
        ReponsePIDEP rep = getClient().sendGrCouleurComp(Integer.parseInt(CouleurCompAnneeField.getText()));

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            DialogGrCouleurComp dial = new DialogGrCouleurComp(this, false, (DonneeGetGrCouleurComp)rep.getChargeUtile());
            dial.setSize(600,500);
            dial.setVisible(true);
        }
        else
        {
            AppLabelError.setText(rep.getMessage());
        }
    }

    private void statInferTestConf()
    {
        ReponsePIDEP rep = getClient().sendGetStatInferTestConf(Integer.parseInt(StatInferConfField.getText()));

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            DialogTestConf dial = new DialogTestConf(this, false, (DonneeGetStatInferTestConf) rep.getChargeUtile());
            dial.setSize(500,200);
            dial.setVisible(true);
        }
        else
        {
            AppLabelError.setText(rep.getMessage());
        }
    }

    private void statInferTestHomog()
    {
        ReponsePIDEP rep = getClient().sendGetStatInferTestHomog(Integer.parseInt(StatInferHomogField.getText()));

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            DialogTestHomog dial = new DialogTestHomog(this, false, (DonneeGetStatInferTestHomog) rep.getChargeUtile());
            dial.setSize(700,300);
            dial.setVisible(true);
        }
        else
        {
            AppLabelError.setText(rep.getMessage());
        }
    }

    private void statInferANOVA()
    {
        ReponsePIDEP rep = getClient().sendGetStatInferANOVA(Integer.parseInt(StatInferANOVAField.getText()));

        System.out.println(" *** Reponse reçue : " + rep.getCode());
        if(rep.getMessage() != null)
        {
            System.out.println("Message reçu: " + rep.getMessage());
        }
        if(rep.getCode() == 200)
        {
            DialogANOVA dial = new DialogANOVA(this, false, (DonneeGetStatInferANOVA) rep.getChargeUtile());
            dial.setSize(700,200);
            dial.setVisible(true);
        }
        else
        {
            AppLabelError.setText(rep.getMessage());
        }
    }

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

        AppLogoutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onAppLogout(evt);
            }
        });

        AppOKButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onAppOK(evt);
            }
        });

        statDescContRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onstatDescCont(evt);
            }
        });

        histCompareRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onhistCompare(evt);
            }
        });

        diagSectorielRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ondiagSectoriel(evt);
            }
        });

        statInferTestConfRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onstatInferTestConf(evt);
            }
        });

        statInferTestHomogRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onstatInferTestHomog(evt);
            }
        });

        statInferANOVARadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onstatInferANOVA(evt);
            }
        });
    }


    /********************************/
    /*             Events           */
    /********************************/
    private void onLoginOK(java.awt.event.ActionEvent evt)
    {
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
            statDescContRadioButton.setSelected(true);
            PanelGrCouleurRep.setVisible(false);
            PanelGrCouleurComp.setVisible(false);
            PanelStatInferTestConf.setVisible(false);
            PanelStatInferTestHomog.setVisible(false);
            PanelStatInferANOVA.setVisible(false);
            PanelStatDescrCont.setVisible(true);

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

    private void onAppLogout(java.awt.event.ActionEvent evt)
    {
        getClient().sendLogout();
        AppPanel.setVisible(false);
        LoginPanel.setVisible(true);
    }

    private void onAppOK(java.awt.event.ActionEvent evt)
    {
        if(statDescContRadioButton.isSelected())
        {
            statDescCont();
        }
        else if(diagSectorielRadioButton.isSelected())
        {
            diagSectoriel();
        }
        else if(histCompareRadioButton.isSelected())
        {
            histCompare();
        }
        else if(statInferTestConfRadioButton.isSelected())
        {
            statInferTestConf();
        }
        else if(statInferTestHomogRadioButton.isSelected())
        {
            statInferTestHomog();
        }
        else if(statInferANOVARadioButton.isSelected())
        {
            statInferANOVA();
        }

    }

    private void onstatDescCont(java.awt.event.ActionEvent evt)
    {
        AppLabelError.setText("");
        PanelGrCouleurRep.setVisible(false);
        PanelStatInferTestConf.setVisible(false);
        PanelStatInferTestHomog.setVisible(false);
        PanelStatInferANOVA.setVisible(false);
        PanelGrCouleurComp.setVisible(false);
        PanelStatDescrCont.setVisible(true);
    }

    private void onhistCompare(java.awt.event.ActionEvent evt)
    {
        AppLabelError.setText("");
        PanelGrCouleurRep.setVisible(false);
        PanelStatInferTestConf.setVisible(false);
        PanelStatInferTestHomog.setVisible(false);
        PanelStatInferANOVA.setVisible(false);
        PanelStatDescrCont.setVisible(false);
        PanelGrCouleurComp.setVisible(true);
    }

    private void ondiagSectoriel(java.awt.event.ActionEvent evt)
    {
        AppLabelError.setText("");
        PanelStatInferTestConf.setVisible(false);
        PanelStatInferTestHomog.setVisible(false);
        PanelStatInferANOVA.setVisible(false);
        PanelStatDescrCont.setVisible(false);
        PanelGrCouleurComp.setVisible(false);
        PanelGrCouleurRep.setVisible(true);
    }

    private void onstatInferTestConf(java.awt.event.ActionEvent evt)
    {
        AppLabelError.setText("");
        PanelGrCouleurRep.setVisible(false);
        PanelStatInferTestHomog.setVisible(false);
        PanelStatInferANOVA.setVisible(false);
        PanelStatDescrCont.setVisible(false);
        PanelGrCouleurComp.setVisible(false);
        PanelStatInferTestConf.setVisible(true);
    }

    private void onstatInferTestHomog(java.awt.event.ActionEvent evt)
    {
        AppLabelError.setText("");
        PanelGrCouleurRep.setVisible(false);
        PanelStatInferTestConf.setVisible(false);
        PanelStatInferANOVA.setVisible(false);
        PanelStatDescrCont.setVisible(false);
        PanelGrCouleurComp.setVisible(false);
        PanelStatInferTestHomog.setVisible(true);
    }

    private void onstatInferANOVA(java.awt.event.ActionEvent evt)
    {
        AppLabelError.setText("");
        PanelGrCouleurRep.setVisible(false);
        PanelStatInferTestConf.setVisible(false);
        PanelStatInferTestHomog.setVisible(false);
        PanelStatDescrCont.setVisible(false);
        PanelGrCouleurComp.setVisible(false);
        PanelStatInferANOVA.setVisible(true);
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