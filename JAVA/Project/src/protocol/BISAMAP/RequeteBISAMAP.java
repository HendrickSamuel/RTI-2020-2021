//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

public class RequeteBISAMAP implements Requete {

    /********************************/
    /*           Variables          */
    /********************************/

    private DonneeRequete chargeUtile;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteBISAMAP(DonneeRequete chargeUtile) {
        this.chargeUtile = chargeUtile;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public DonneeRequete getChargeUtile() {
        return chargeUtile;
    }

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}
