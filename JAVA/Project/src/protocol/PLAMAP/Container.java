//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import java.io.Serializable;

public class Container implements Serializable
{
    private static final long serialVersionUID = 8598288074923892238L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String id;
    private int x;
    private int y;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Container()
    {

    }

    public Container(String id, int x, int y)
    {
        this.id = id;
        this.x = x;
        this.y = y;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getId()
    {
        return id;
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
    public void setId(String id)
    {
        this.id = id;
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
        return getId() + "@" + getX() + "@" + getY() + "/";
    }
}