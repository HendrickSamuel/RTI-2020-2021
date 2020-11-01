//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 01/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;

import java.io.Serializable;

public class ReponsePIDEP implements Reponse, Serializable {
    private static final long serialVersionUID = 2673787521001662610L;

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

    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public int getCode() {
        return 0;
    }

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}
