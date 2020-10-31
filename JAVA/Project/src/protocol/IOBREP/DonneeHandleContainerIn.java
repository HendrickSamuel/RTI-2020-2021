//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeHandleContainerIn implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -7144158552582997713L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String idContainer;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeHandleContainerIn(String idContainer) {
        this.idContainer = idContainer;
    }

    public DonneeHandleContainerIn() {
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
