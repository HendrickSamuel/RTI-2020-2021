//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 14/12/2020

package protocol.CSA;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;

public class TraitementCSA implements Traitement
{
    @Override
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException {
        return null;
    }

    @Override
    public void AfficheTraitement(String message) {

    }

    @Override
    public void setConsole(ConsoleServeur cs) {

    }

    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}