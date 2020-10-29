//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DoneeBoatLeft implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -9119628162186033820L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String idContainer;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DoneeBoatLeft(String idContainer) {
        this.idContainer = idContainer;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getIdContainer() {
        return idContainer;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdContainer(String idContainer) {
        this.idContainer = idContainer;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public void setFiledsFromString(String fields) {

    }

}
