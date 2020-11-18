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
import protocol.CHAMAP.ReponseCHAMAP;
import protocol.CHAMAP.TraitementCHAMAP;
import protocol.TRAMAP.ReponseTRAMAP;
import security.SecurityHelper;

public class ServeurComptaCHAMAP extends ServeurGenerique {

    /********************************/
    /*           Variables          */
    /********************************/
    private MysqlConnector _bd;
    private SecurityHelper _sc;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurComptaCHAMAP(int port, boolean connecte, int NbThreads, MysqlConnector bd, ConsoleServeur cs, SecurityHelper sc ,boolean isJavaCommunication ) {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
        this._bd = bd;
        this._sc = sc;
    }

    public ServeurComptaCHAMAP(int port, boolean connecte, int NbThreads, MysqlConnector bd, ConsoleServeur cs, SecurityHelper sc) {
        super(port, connecte, NbThreads, cs, true);
        this._bd = bd;
        this._sc = sc;
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement() {

        return new TraitementCHAMAP(_sc, _bd, _console);
    }

    @Override
    public Reponse CreateBusyResponse() {
        return new ReponseCHAMAP(ReponseTRAMAP.NOK, "BUSY - try again later", null);
    }
}
