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
    private String societe;
    private String destination;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeInputLoryWithoutReservation(String idContainer, String immatriculation, String societe, String destination)
    {
        setIdContainer(idContainer);
        setImmatriculation(immatriculation);
        setDestination(destination);
        setSociete(societe);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getIdContainer()
    {
        return idContainer;
    }

    public String getImmatriculation()
    {
        return immatriculation;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getSociete()
    {
        return societe;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

    public void setImmatriculation(String immatriculation)
    {
        this.immatriculation = immatriculation;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public void setSociete(String societe)
    {
        this.societe = societe;
    }
}
