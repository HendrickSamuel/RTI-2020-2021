//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;

public class ReponseIOBREP implements Reponse {

    /********************************/
    /*           Variables          */
    /********************************/

    private int _code;
    private DonneeRequete _chargeUtile;
    private String _message;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ReponseIOBREP(int _code, DonneeRequete _chargeUtile, String _message) {
        this._code = _code;
        this._chargeUtile = _chargeUtile;
        this._message = _message;
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
    public int getCode() {
        return _code;
    }

    public DonneeRequete get_chargeUtile() {
        return _chargeUtile;
    }

    public String get_message() {
        return _message;
    }
}
