//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeGetXY implements DonneeRequete, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String societe;
    private String immatriculationCamion;
    private String idContainer;

    private String numReservation;
    private int x;
    private int y;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetXY()
    {

    }

    public DonneeGetXY(String numReservation, int x, int y)
    {
        this.numReservation = numReservation;
        this.x = x;
        this.y = y;
    }

    public DonneeGetXY(String societe, String immatriculationCamion, String idContainer)
    {
        this.societe = societe;
        this.immatriculationCamion = immatriculationCamion;
        this.idContainer = idContainer;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getSociete()
    {
        return societe;
    }

    public String getImmatriculationCamion()
    {
        return immatriculationCamion;
    }

    public String getIdContainer()
    {
        return idContainer;
    }

    public String getNumReservation()
    {
        return numReservation;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setSociete(String societe)
    {
        this.societe = societe;
    }

    public void setImmatriculationCamion(String immatriculationCamion)
    {
        this.immatriculationCamion = immatriculationCamion;
    }

    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

    public void setNumReservation(String numReservation)
    {
        this.numReservation = numReservation;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return "DonneeGetXY{" +
                "numReservation='" + numReservation + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}