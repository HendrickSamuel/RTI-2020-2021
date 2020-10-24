//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package protocol.IOBREP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDCompta;
import lib.BeanDBAcces.BDMouvements;
import lib.BeanDBAcces.DataSource;

public class TraitementIOBREP implements Traitement {

    /********************************/
    /*           Variables          */
    /********************************/

    private BDMouvements _bdMouvement;
    private BDCompta _bdCompta;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementIOBREP(BDMouvements _bdMouvement, BDCompta _bdCompta) {
        this._bdMouvement = _bdMouvement;
        this._bdCompta = _bdCompta;
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    @Override
    public void setConsole(ConsoleServeur cs) {

    }

    public void set_bdMouvement(BDMouvements _bdMouvement) {
        this._bdMouvement = _bdMouvement;
    }

    public void set_bdCompta(BDCompta _bdCompta) {
        this._bdCompta = _bdCompta;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException {
        return null;
    }

    @Override
    public void AfficheTraitement(String message) {

    }

}
