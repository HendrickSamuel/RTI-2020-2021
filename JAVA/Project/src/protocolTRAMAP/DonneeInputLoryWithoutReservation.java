/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeInputLoryWithoutReservation implements DonneeRequete, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String idContainer;
    private String immatriculation;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeInputLoryWithoutReservation(String idContainer, String immatriculation)
    {
        setIdContainer(idContainer);
        setImmatriculation(immatriculation);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getIdContainer()
    {
        return idContainer;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
}
