//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeSendWeight implements DonneeRequete, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String idContainer;
    private int x;
    private int y;
    private float poids;
    private int type;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeSendWeight()
    {
        this.type = 3;
    }

    public DonneeSendWeight(String idContainer, int x, int y, float poids)
    {
        this.idContainer = idContainer;
        this.x = x;
        this.y = y;
        this.poids = poids;
        this.type = 3;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getType()
    {
        return type;
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

    public float getPoids()
    {
        return poids;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdContainer(String idContainer)
    {
        this.idContainer = idContainer;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setPoids(float poids)
    {
        this.poids = poids;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return  getType() + "";
    }

    @Override
    public void setFiledsFromString(String fields) {
        String[] parametres = fields.split("#");
        String[] row;
        for(int i = 1; i < parametres.length; i++)
        {
            row = parametres[i].split("=");
            switch (row[i])
            {
                case "idContainer": this.setIdContainer(row[1]); break;
                case "x": this.setX(Integer.parseInt(row[1])); break;
                case "y": this.setY(Integer.parseInt(row[1])); break;
                case "poids": this.setPoids(Float.parseFloat(row[1])); break;
            }
        }
    }
}