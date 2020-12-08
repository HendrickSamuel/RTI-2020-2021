//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package protocol.PFMCOP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;
import java.io.Serializable;

public class RequetePFMCOP  implements Requete, Serializable
{
    private static final long serialVersionUID = 1653735157842857748L;

    /********************************/
    /*           Variables          */
    /********************************/
    private DonneeRequete chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequetePFMCOP()
    {

    }

    public RequetePFMCOP(DonneeRequete chargeUtile)
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