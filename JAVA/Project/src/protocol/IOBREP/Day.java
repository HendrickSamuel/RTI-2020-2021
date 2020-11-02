//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.IOBREP;

import java.io.Serializable;

public class Day implements Serializable {
    private static final long serialVersionUID = 3026903785198951071L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String day;
    private int containersCharges;
    private int containersDecharges;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Day(String day, int containersCharges, int containersDecharges) {
        this.day = day;
        this.containersCharges = containersCharges;
        this.containersDecharges = containersDecharges;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getDay() {
        return day;
    }

    public int getContainersCharges() {
        return containersCharges;
    }

    public int getContainersDecharges() {
        return containersDecharges;
    }
    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}
