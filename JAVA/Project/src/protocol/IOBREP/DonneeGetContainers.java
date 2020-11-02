//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.List;

public class DonneeGetContainers implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -14106860632702088L;

    /********************************/
    /*           Variables          */
    /********************************/

    private String destination;
    private String selection; //FIRST RANDOM

    private String mode;
    private String idBateau;

    private List<Container> _containers;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetContainers(String destination, String selection, String mode) {
        this.destination = destination;
        this.selection = selection;
        this.mode = mode;
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

    public List<Container> get_containers() {
        return _containers;
    }

    public String getMode() {
        return mode;
    }

    public String getIdBateau() {
        return idBateau;
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

    public void set_containers(List<Container> _containers) {
        this._containers = _containers;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
