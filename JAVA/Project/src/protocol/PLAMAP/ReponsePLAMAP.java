//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import java.io.Serializable;

public class ReponsePLAMAP implements Reponse, Serializable
{
    private static final long serialVersionUID = 928512451231093947L;
    /********************************/
    /*           Variables          */
    /********************************/
    public static int OK = 200;
    public static int NOK = 400;
    public static int REQUEST_NOT_FOUND = 404;

    private int codeRetour;
    private String message;
    private DonneeRequete chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ReponsePLAMAP()
    {

    }

    public ReponsePLAMAP(int codeRetour, String message, DonneeRequete chargeUtile)
    {
        this.codeRetour = codeRetour;
        this.message = message;
        this.chargeUtile = chargeUtile;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public int getCode()
    {
        return codeRetour;
    }

    public String getMessage()
    {
        return message;
    }

    public DonneeRequete getChargeUtile()
    {
        return chargeUtile;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setCodeRetour(int codeRetour)
    {
        this.codeRetour = codeRetour;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setChargeUtile(DonneeRequete chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        if(getCode() == OK)
        {
            return "true#" + getChargeUtile().toString() + "#%";
        }
        return "false#" + getMessage() + "#%";
    }


}