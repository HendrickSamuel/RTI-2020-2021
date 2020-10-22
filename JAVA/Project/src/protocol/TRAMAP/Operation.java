//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package protocol.TRAMAP;

import java.util.Date;

public class Operation {

    /********************************/
    /*           Variables          */
    /********************************/
    private int _id;
    private String _container;
    private String _transporteurEntrant;
    private Date _dateArrivee;
    private String _transporteurSortant;
    private Float _poidsTotal;
    private Float _poidsDepart;
    private Date _dateDepart;
    private String _destination;

    /********************************/
    /*         Constructeurs        */
    /********************************/

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_container(String _container) {
        this._container = _container;
    }

    public void set_transporteurEntrant(String _transporteurEntrant) {
        this._transporteurEntrant = _transporteurEntrant;
    }

    public void set_dateArrivee(Date _dateArrivee) {
        this._dateArrivee = _dateArrivee;
    }

    public void set_transporteurSortant(String _transporteurSortant) {
        this._transporteurSortant = _transporteurSortant;
    }

    public void set_poidsTotal(Float _poidsTotal) {
        this._poidsTotal = _poidsTotal;
    }

    public void set_poidsDepart(Float _poidsDepart) {
        this._poidsDepart = _poidsDepart;
    }

    public void set_dateDepart(Date _dateDepart) {
        this._dateDepart = _dateDepart;
    }

    public void set_destination(String _destination) {
        this._destination = _destination;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

}
