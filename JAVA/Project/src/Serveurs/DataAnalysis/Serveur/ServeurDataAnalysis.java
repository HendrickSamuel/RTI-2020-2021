//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package Serveurs.DataAnalysis.Serveur;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;
import protocol.PIDEP.TraitementPIDEP;

public class ServeurDataAnalysis extends ServeurGenerique
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bdMouvement;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurDataAnalysis(int port, boolean connecte, int NbThreads, ConsoleServeur cs, boolean isJavaCommunication)
    {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public BDMouvements get_bdMouvement()
    {
        return _bdMouvement;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bdMouvement(BDMouvements _bdMouvement)
    {
        this._bdMouvement = _bdMouvement;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement()
    {
        return new TraitementPIDEP(_bdMouvement);
    }

    @Override
    public Reponse CreateBusyResponse()
    {
        return null;
    }
}