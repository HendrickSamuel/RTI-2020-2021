//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

import java.io.Serializable;

public class RequeteIOBREP implements Requete, Serializable {

    /********************************/
    /*           Variables          */
    /********************************/
    private DonneeRequete chargeUtile;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteIOBREP(DonneeRequete chargeUtile) {
        this.chargeUtile = chargeUtile;
    }
    /********************************/
    /*            Getters           */
    /********************************/
    public DonneeRequete getChargeUtile() {
        return chargeUtile;
    }
    /********************************/
    /*            Setters           */
    /********************************/
    public void setChargeUtile(DonneeRequete chargeUtile) {
        this.chargeUtile = chargeUtile;
    }
    /********************************/
    /*            Methodes          */
    /********************************/

}
