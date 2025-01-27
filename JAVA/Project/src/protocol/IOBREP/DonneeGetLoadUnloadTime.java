//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.List;

public class DonneeGetLoadUnloadTime implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -2500469149695169197L;

    /********************************/
    /*           Variables          */
    /********************************/
    private List<Docker> dockers;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetLoadUnloadTime() {
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public List<Docker> getDockers() {
        return dockers;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setDockers(List<Docker> dockers) {
        this.dockers = dockers;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
