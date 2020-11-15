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

public class ServeurComptaCHAMAP extends ServeurGenerique {

    /********************************/
    /*           Variables          */
    /********************************/
    private MysqlConnector _bd;
    private String _provider;
    private String _hash;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurComptaCHAMAP(int port, boolean connecte, int NbThreads, MysqlConnector bd, ConsoleServeur cs, String provider, String hash ,boolean isJavaCommunication ) {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
        this._bd = bd;
        this._provider = provider;
        this._hash = hash;
    }

    public ServeurComptaCHAMAP(int port, boolean connecte, int NbThreads, MysqlConnector bd, ConsoleServeur cs, String provider, String hash) {
        super(port, connecte, NbThreads, cs, true);
        this._bd = bd;
        this._provider = provider;
        this._hash = hash;
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

        return new TraitementCHAMAP(_provider, _hash, _bd, _console);
    }

    @Override
    public Reponse CreateBusyResponse() {
        return new ReponseCHAMAP(ReponseTRAMAP.NOK, "BUSY - try again later", null);
    }
}
