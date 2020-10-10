/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import genericRequest.Reponse;
import java.io.Serializable;
import java.util.Properties;

public class ReponseTRAMAP implements Reponse, Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    public static int OK = 200;
    public static int LOGIN_OK = 201;
    public static int BAD_DATA = 401;
    public static int REQUEST_NOT_FOUND = 404;
    private int codeRetour;
    private String message;
    private DonneesTRAMAP chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ReponseTRAMAP(int c, DonneesTRAMAP chu, String msg)
    {
        codeRetour = c;
        setChargeUtile(chu);
        message = msg;
    }


    /********************************/
    /*           Getters            */
    /********************************/
    public int getCode()
    {
        return codeRetour;
    }

    public DonneesTRAMAP getChargeUtile()
    {
        return chargeUtile;
    }


    /********************************/
    /*           Setters            */
    /********************************/
    public void setChargeUtile(DonneesTRAMAP chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

}
