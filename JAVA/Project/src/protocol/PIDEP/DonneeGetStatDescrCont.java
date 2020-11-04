//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeGetStatDescrCont implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -3108475313613148750L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _tailleEch;
    private boolean _entree;

    private double _moyenne;
    private double _ecartType;
    private double _mode;
    private double _mediane;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetStatDescrCont()
    {

    }

    public DonneeGetStatDescrCont(int _tailleEch, boolean _entree)
    {
        this._tailleEch = _tailleEch;
        this._entree = _entree;
    }

    public DonneeGetStatDescrCont(double _moyenne, double _ecartType, double _mode, double _mediane)
    {
        this._moyenne = _moyenne;
        this._ecartType = _ecartType;
        this._mode = _mode;
        this._mediane = _mediane;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public int get_tailleEch()
    {
        return _tailleEch;
    }

    public boolean is_entree()
    {
        return _entree;
    }

    public double get_moyenne()
    {
        return _moyenne;
    }

    public double get_ecartType()
    {
        return _ecartType;
    }

    public double get_mode()
    {
        return _mode;
    }

    public double get_mediane()
    {
        return _mediane;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_tailleEch(int _tailleEch)
    {
        this._tailleEch = _tailleEch;
    }

    public void set_entree(boolean _entree)
    {
        this._entree = _entree;
    }

    public void set_moyenne(double _moyenne)
    {
        this._moyenne = _moyenne;
    }

    public void set_ecartType(double _ecartType)
    {
        this._ecartType = _ecartType;
    }

    public void set_mode(double _mode)
    {
        this._mode = _mode;
    }

    public void set_mediane(double _mediane)
    {
        this._mediane = _mediane;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}