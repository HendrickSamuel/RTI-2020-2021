//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

public class RequeteSAMOP implements Requete
{
    private static final long serialVersionUID = 1263265093496358851L;

    /********************************/
    /*           Variables          */
    /********************************/
    private DonneeRequete _chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteSAMOP()
    {

    }
    public RequeteSAMOP(DonneeRequete _chargeUtile)
    {
        this._chargeUtile = _chargeUtile;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public DonneeRequete getChargeUtile()
    {
        return _chargeUtile;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_chargeUtile(DonneeRequete _chargeUtile)
    {
        this._chargeUtile = _chargeUtile;
    }


    /********************************/
    /*            Methodes          */
    /********************************/

}