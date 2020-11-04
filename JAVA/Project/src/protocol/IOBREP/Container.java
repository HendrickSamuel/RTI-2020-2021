//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 25/10/2020

package protocol.IOBREP;

import java.io.Serializable;

public class Container implements Serializable {

    /********************************/
    /*           Variables          */
    /********************************/
    private String id;
    private int x;
    private int y;

    /********************************/
    /*         Constructeurs        */
    /********************************/

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

}
