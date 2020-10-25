//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package protocol.TRAMAP;

import java.util.Date;

public class Operation
{

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
    public Operation()
    {

    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int get_id()
    {
        return _id;
    }

    public String get_container()
    {
        return _container;
    }

    public String get_transporteurEntrant()
    {
        return _transporteurEntrant;
    }

    public Date get_dateArrivee() {
        return _dateArrivee;
    }

    public String get_transporteurSortant()
    {
        return _transporteurSortant;
    }

    public Float get_poidsTotal()
    {
        return _poidsTotal;
    }

    public Float get_poidsDepart()
    {
        return _poidsDepart;
    }

    public Date get_dateDepart()
    {
        return _dateDepart;
    }

    public String get_destination()
    {
        return _destination;
    }


    /********************************/
    /*            Setters           */
    /********************************/

    public void set_id(int _id)
    {
        this._id = _id;
    }

    public void set_container(String _container)
    {
        this._container = _container;
    }

    public void set_transporteurEntrant(String _transporteurEntrant)
    {
        this._transporteurEntrant = _transporteurEntrant;
    }

    public void set_dateArrivee(Date _dateArrivee)
    {
        this._dateArrivee = _dateArrivee;
    }

    public void set_transporteurSortant(String _transporteurSortant)
    {
        this._transporteurSortant = _transporteurSortant;
    }

    public void set_poidsTotal(Float _poidsTotal)
    {
        this._poidsTotal = _poidsTotal;
    }

    public void set_poidsDepart(Float _poidsDepart)
    {
        this._poidsDepart = _poidsDepart;
    }

    public void set_dateDepart(Date _dateDepart)
    {
        this._dateDepart = _dateDepart;
    }

    public void set_destination(String _destination)
    {
        this._destination = _destination;
    }



    /********************************/
    /*            Methodes          */
    /********************************/

}
