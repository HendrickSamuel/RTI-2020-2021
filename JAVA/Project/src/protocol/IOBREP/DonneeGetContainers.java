//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeGetContainers implements DonneeRequete, Serializable {

    /********************************/
    /*           Variables          */
    /********************************/

    private String destination;
    private String selection; //FIRST RANDOM

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetContainers(String destination, String selection) {
        this.destination = destination;
        this.selection = selection;
    }
    /********************************/
    /*            Getters           */
    /********************************/
    public String getDestination() {
        return destination;
    }

    public String getSelection() {
        return selection;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

}
