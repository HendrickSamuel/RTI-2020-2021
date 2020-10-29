//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeHandleContainerOut implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -4935605981816200705L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String idBateau;
    private String idContainer;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeHandleContainerOut(String idContainer) {
        this.idContainer = idContainer;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getIdContainer() {
        return idContainer;
    }

    public String getIdBateau() {
        return idBateau;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdContainer(String idContainer) {
        this.idContainer = idContainer;
    }

    public void setIdBateau(String idBateau) {
        this.idBateau = idBateau;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public void setFiledsFromString(String fields) {

    }

}
