//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package Serveurs.Compta.Serveur;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.MysqlConnector;
import protocol.BISAMAP.ReponseBISAMAP;
import protocol.BISAMAP.TraitementBISAMAP;
import protocol.CHAMAP.TraitementCHAMAP;
import security.SecurityHelper;

public class ServeurComptaBISAMAP extends ServeurGenerique {

    /********************************/
    /*           Variables          */
    /********************************/
    private MysqlConnector bd_comtpa;
    private SecurityHelper securityHelper;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurComptaBISAMAP(int port, boolean connecte, int NbThreads, ConsoleServeur cs, boolean isJavaCommunication) {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/
    public void setBd_comtpa(MysqlConnector bd_comtpa) {
        this.bd_comtpa = bd_comtpa;
    }

    public void setSecurityHelper(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement() {
        return new TraitementBISAMAP(_console, bd_comtpa, securityHelper);
    }

    @Override
    public Reponse CreateBusyResponse() {
        return new ReponseBISAMAP(ReponseBISAMAP.NOK, "Not enought ressources to allocate to you" ,null);
    }
}
