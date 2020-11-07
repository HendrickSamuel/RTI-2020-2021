//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Serveur;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.RServe;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDDecisions;
import lib.BeanDBAcces.BDMouvements;
import protocol.PIDEP.ReponsePIDEP;
import protocol.PIDEP.TraitementPIDEP;


public class ServeurDataAnalysis extends ServeurGenerique
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bdMouvement;
    private BDDecisions _bdDecision;
    private RServe _rServe;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurDataAnalysis(int port, boolean connecte, int NbThreads, BDMouvements _bdMouvements, BDDecisions _bdDecision, RServe _rServe, ConsoleServeur cs)
    {
        super(port, connecte, NbThreads, cs, true);
        this._bdMouvement = _bdMouvements;
        this._bdDecision = _bdDecision;
        this._rServe = _rServe;
    }

    public ServeurDataAnalysis(int port, boolean connecte, int NbThreads, BDMouvements _bdMouvements, BDDecisions _bdDecision, RServe _rServe, ConsoleServeur cs, boolean isJavaCommunication)
    {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
        this._bdMouvement = _bdMouvements;
        this._bdDecision = _bdDecision;
        this._rServe = _rServe;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public BDMouvements get_bdMouvement()
    {
        return _bdMouvement;
    }

    public BDDecisions get_bdDecision()
    {
        return _bdDecision;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bdMouvement(BDMouvements _bdMouvement)
    {
        this._bdMouvement = _bdMouvement;
    }

    public void set_bdDecision(BDDecisions _bdDecision)
    {
        this._bdDecision = _bdDecision;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement()
    {
        TraitementPIDEP tp = new TraitementPIDEP();
        tp.setConsole(this._console);
        tp.setBdMouvements(this._bdMouvement);
        tp.setBdDecisions(this._bdDecision);
        tp.setRServe(this._rServe);
        return tp;
    }

    @Override
    public Reponse CreateBusyResponse()
    {
        return new ReponsePIDEP(ReponsePIDEP.NOK, "Plus de ressource disponible", null);
    }
}