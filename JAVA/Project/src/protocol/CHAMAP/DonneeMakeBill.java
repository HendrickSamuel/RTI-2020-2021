//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.CHAMAP;

import genericRequest.DonneeRequete;

import java.util.List;

public class DonneeMakeBill implements DonneeRequete
{
    private static final long serialVersionUID = -750166523178324269L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _societe;
    private String _transporteur;
    private String _destination;
    private List<String> _containers;
    private List<Integer> _mouvements;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeMakeBill() {
    }

    public DonneeMakeBill(String _societe, String _transporteur, String _destination, List<String> _containers, List<Integer> _mouvements) {
        this._societe = _societe;
        this._transporteur = _transporteur;
        this._destination = _destination;
        this._containers = _containers;
        this._mouvements = _mouvements;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String get_transporteur() {
        return _transporteur;
    }

    public List<String> get_containers() {
        return _containers;
    }

    public String get_societe() {
        return _societe;
    }

    public List<Integer> get_mouvements() {
        return _mouvements;
    }

    public String get_destination() {
        return _destination;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_transporteur(String _transporteur) {
        this._transporteur = _transporteur;
    }

    public void set_containers(List<String> _containers) {
        this._containers = _containers;
    }

    public void set_societe(String _societe) {
        this._societe = _societe;
    }

    public void set_mouvements(List<Integer> _mouvements) {
        this._mouvements = _mouvements;
    }

    public void set_destination(String _destination) {
        this._destination = _destination;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
