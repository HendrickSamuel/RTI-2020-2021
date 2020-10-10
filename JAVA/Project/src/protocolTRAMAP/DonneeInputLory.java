/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeInputLory implements DonneeRequete, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String numeroReservation;
    private String idContainer;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeInputLory(String numeroReservation, String idContainer)
    {
        this.numeroReservation = numeroReservation;
        this.idContainer = idContainer;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getNumeroReservation()
    {
        return numeroReservation;
    }

    public String getIdContainer()
    {
        return idContainer;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setNumeroReservation(String numeroReservation)
    {
        this.numeroReservation = numeroReservation;
    }

    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

}