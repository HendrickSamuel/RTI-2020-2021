/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import java.io.Serializable;

public class DonneeInputLoryWithoutReservation implements DonneesTRAMAP, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String idContainer;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeInputLoryWithoutReservation(String idContainer)
    {
        this.idContainer = idContainer;
    }


    /********************************/
    /*           Getters            */
    /********************************/
    public String getIdContainer()
    {
        return idContainer;
    }


    /********************************/
    /*           Setters            */
    /********************************/
    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

}
