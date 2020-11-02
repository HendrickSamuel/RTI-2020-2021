//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 01/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

import java.io.Serializable;

public class RequetePIDEP implements Requete, Serializable {
    private static final long serialVersionUID = 2033773283556338797L;

    /********************************/
    /*           Variables          */
    /********************************/
    private DonneeRequete chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequetePIDEP()
    {

    }

    public RequetePIDEP(DonneeRequete chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public DonneeRequete getChargeUtile()
    {
        return chargeUtile;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setChargeUtile(DonneeRequete chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }


    /********************************/
    /*            Methodes          */
    /********************************/

}
