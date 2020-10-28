//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.List;

public class DonneeGetList implements DonneeRequete, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String idTransporteur;
    private String destination;
    private int nombreMax;
    private int type;

    private List<Container> listCont;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetList()
    {
      this.type = 4;
    }

    public DonneeGetList(String idTransporteur, String destination, int nombreMax)
    {
        this.idTransporteur = idTransporteur;
        this.destination = destination;
        this.nombreMax = nombreMax;
        this.type = 4;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getType()
    {
        return type;
    }

    public String getIdTransporteur()
    {
        return idTransporteur;
    }

    public String getDestination()
    {
        return destination;
    }

    public int getNombreMax()
    {
        return nombreMax;
    }

    public List<Container> getListCont()
    {
        return listCont;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdTransporteur(String idTransporteur)
    {
        this.idTransporteur = idTransporteur;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public void setNombreMax(int nombreMax)
    {
        this.nombreMax = nombreMax;
    }

    public void setListCont(List<Container> listCont)
    {
        this.listCont = listCont;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        String s = getType() + "#";

        for(int i = 0 ; i < getListCont().size() ; i++)
        {
            s = s + getListCont().get(i).toString();
        }

        return s;
    }
}