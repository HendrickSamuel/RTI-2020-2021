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
    private String destination;

    private int type;
    private String numReservation;
    private int x;
    private int y;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetXY()
    {
        this.type = 2;
    }

    public DonneeGetXY(String numReservation, int x, int y)
    {
        this.numReservation = numReservation;
        this.x = x;
        this.y = y;
        this.type = 2;
    }

    public DonneeGetXY(String societe, String immatriculationCamion, String idContainer, String destination)
    {
        this.societe = societe;
        this.immatriculationCamion = immatriculationCamion;
        this.idContainer = idContainer;
        this.destination = destination;
        this.type = 2;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getType()
    {
        return type;
    }

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

    public String getDestination() {
        return destination;
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

    public void setDestination(String destination) {
        this.destination = destination;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return getType() + "#" + getNumReservation() + "#" + getX() + "/" + getY();
    }

    @Override
    public void setFiledsFromString(String fields) {
        String[] parametres = fields.split("#");
        String[] row;
        for(int i = 1; i < parametres.length; i++)
        {
            row = parametres[i].split("=");
            switch (row[0])
            {
                case "societe": this.setSociete(row[1]); break;
                case "immatriculationCamion": this.setImmatriculationCamion(row[1]); break;
                case "idContainer": this.setIdContainer(row[1]); break;
                case "destination": this.setDestination(row[1]); break;
            }
        }
    }

}