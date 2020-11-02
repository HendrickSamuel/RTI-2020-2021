//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.List;

public class DonneeGetLoadUnloadStats implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -8950284212428233184L;

    /********************************/
    /*           Variables          */
    /********************************/

    private List<Day> jours;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetLoadUnloadStats() {
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public List<Day> getJours() {
        return jours;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setJours(List<Day> jours) {
        this.jours = jours;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public void setFiledsFromString(String fields) {

    }

}
