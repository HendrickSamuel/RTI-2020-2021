//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package Serveurs.Bateaux;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;
import protocol.IOBREP.ReponseIOBREP;
import protocol.IOBREP.TraitementIOBREP;

public class ServeurBateaux extends ServeurGenerique
{

    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bdMouvement;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurBateaux(int port, boolean connecte, int NbThreads, ConsoleServeur cs)
    {
        super(port, connecte, NbThreads, cs, true);
    }

    public ServeurBateaux(int port, boolean connecte, int NbThreads, ConsoleServeur cs, boolean isJavaCommunication)
    {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
    }

    /********************************/
    /*            Getters           */
    /********************************/

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
        return new TraitementIOBREP(_bdMouvement, null);
    }

    @Override
    public Reponse CreateBusyResponse() {
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Plus de ressources disponibles");
    }
}
