//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeGetStatInferTestConf implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -2710837771185489568L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _taillEch;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetStatInferTestConf()
    {

    }

    public DonneeGetStatInferTestConf(int _taillEch)
    {
        this._taillEch = _taillEch;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int get_taillEch()
    {
        return _taillEch;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_taillEch(int _taillEch)
    {
        this._taillEch = _taillEch;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}