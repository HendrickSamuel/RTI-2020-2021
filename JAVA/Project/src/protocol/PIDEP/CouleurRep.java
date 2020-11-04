//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 04/11/2020

package protocol.PIDEP;

import java.io.Serializable;

public class CouleurRep implements Serializable
{
    private static final long serialVersionUID = -8671390182263408580L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _destination;
    private int _nombre;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public CouleurRep()
    {

    }

    public CouleurRep(String _destination, int _nombre)
    {
        this._destination = _destination;
        this._nombre = _nombre;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_destination()
    {
        return _destination;
    }

    public int get_nombre()
    {
        return _nombre;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_destination(String _destination)
    {
        this._destination = _destination;
    }

    public void set_nombre(int _nombre)
    {
        this._nombre = _nombre;
    }


    /********************************/
    /*            Methodes          */
    /********************************/

}