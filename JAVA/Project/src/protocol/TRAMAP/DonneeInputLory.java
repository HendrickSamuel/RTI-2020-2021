/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocol.TRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeInputLory implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -5255810545824479653L;
    /********************************/
    /*           Variables          */
    /********************************/
    private String numeroReservation;
    private String idTransporteur;
    private String idContainer;
    private String destination;

    private int x;
    private int y;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeInputLory(String numeroReservation, String idTransporteur, String idContainer, String destination) {
        this.numeroReservation = numeroReservation;
        this.idTransporteur = idTransporteur;
        this.idContainer = idContainer;
        this.destination = destination;
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

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getIdTransporteur() {
        return idTransporteur;
    }

    public String getDestination() {
        return destination;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setNumeroReservation(String numeroReservation)
    {
        this.numeroReservation = numeroReservation;
    }

    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

    public void setIdTransporteur(String idTransporteur) {
        this.idTransporteur = idTransporteur;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}