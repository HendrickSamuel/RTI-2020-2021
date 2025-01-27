//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeEndContainerIn implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -8210794890764332648L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String idBateau;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeEndContainerIn(String idBateau) {
        this.idBateau = idBateau;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getIdBateau() {
        return idBateau;
    }

    /********************************/
    /*            Setters           */
    /********************************/
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
