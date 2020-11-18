//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.CHAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

public class RequeteCHAMAP implements Requete
{
    private static final long serialVersionUID = -3329135473673422204L;

    /********************************/
    /*           Variables          */
    /********************************/

    private DonneeRequete _chargeUtile;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteCHAMAP(DonneeRequete _chargeUtile) {
        this._chargeUtile = _chargeUtile;
    }
    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public DonneeRequete getChargeUtile() {
        return _chargeUtile;
    }
    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}
