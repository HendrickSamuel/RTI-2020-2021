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
    private static final long serialVersionUID = -8964289158795781207L;
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

        if(getListCont() != null)
        {
            for (int i = 0; i < getListCont().size(); i++)
            {
                s = s + getListCont().get(i).toString();
            }
        }

        return s;
    }

    @Override
    public void setFiledsFromString(String fields)
    {
        String[] parametres = fields.split("#");
        String[] row;
        for(int i = 1; i < parametres.length; i++)
        {
            row = parametres[i].split("=");
            switch (row[0])
            {
                case "idTransporteur": this.setIdTransporteur(row[1]); break;
                case "destination": this.setDestination(row[1]); break;
                case "nombreMax": this.setNombreMax(Integer.parseInt(row[1])); break;
            }
        }
    }
}