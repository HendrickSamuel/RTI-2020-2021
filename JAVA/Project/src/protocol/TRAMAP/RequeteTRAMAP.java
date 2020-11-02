/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocol.TRAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

import java.io.Serializable;


public class RequeteTRAMAP implements Requete, Serializable
{
    private static final long serialVersionUID = 6695039056227684573L;
    /********************************/
    /*          Variables           */
    /********************************/
    private DonneeRequete chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteTRAMAP(DonneeRequete chu)
    {
        chargeUtile = chu;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public DonneeRequete getChargeUtile()
    {
        return chargeUtile;
    }
}
