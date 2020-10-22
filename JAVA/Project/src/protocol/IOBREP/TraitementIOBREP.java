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
import lib.BeanDBAcces.DataSource;

public class TraitementIOBREP implements Traitement {

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

    @Override
    public void setConsole(ConsoleServeur cs) {

    }

    @Override
    public void setDataSource(DataSource ds) throws ClassCastException {

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
