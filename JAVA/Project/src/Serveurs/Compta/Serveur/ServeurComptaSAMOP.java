//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package Serveurs.Compta.Serveur;

import Mails.MailHelper;
import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.MyProperties;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDCompta;
import protocol.SAMOP.ReponseSAMOP;
import protocol.SAMOP.TraitementSAMOP;
import security.SecurityHelper;

public class ServeurComptaSAMOP extends ServeurGenerique
{

    /********************************/
    /*           Variables          */
    /********************************/
    BDCompta _bdCompta;
    private SecurityHelper securityHelper;
    private MailHelper mailHelper;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurComptaSAMOP(int port, boolean connecte, int NbThreads, BDCompta db, ConsoleServeur cs)
    {
        super(port, connecte, NbThreads, cs, true);
        set_bdCompta(db);

        MyProperties mp = new MyProperties("./Confs/MailClient.conf");
        mailHelper = new MailHelper();
        mailHelper.set_userAdresse(mp.getContent("USERNAME"));
        mailHelper.set_password(mp.getContent("PASSWORD"));
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public BDCompta get_bdCompta()
    {
        return _bdCompta;
    }

    public SecurityHelper getSecurityHelper()
    {
        return securityHelper;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bdCompta(BDCompta _bdCompta)
    {
        this._bdCompta = _bdCompta;
    }

    public void setSecurityHelper(SecurityHelper securityHelper)
    {
        this.securityHelper = securityHelper;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement()
    {
        TraitementSAMOP ts = new TraitementSAMOP();
        ts.setConsole(this._console);
        ts.setBd_compta(this._bdCompta);
        ts.set_sc(this.securityHelper);
        ts.set_mails(this.mailHelper);
        return ts;
    }

    @Override
    public Reponse CreateBusyResponse()
    {
        return new ReponseSAMOP(ReponseSAMOP.NOK, "Plus de ressource disponible", null);
    }
}