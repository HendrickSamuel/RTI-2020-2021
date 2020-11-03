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


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}