//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeGetStatInferTestHomog implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 3275793837157702544L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _tailleEch;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetStatInferTestHomog()
    {

    }

    public DonneeGetStatInferTestHomog(int _tailleEch)
    {
        this._tailleEch = _tailleEch;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int get_tailleEch()
    {
        return _tailleEch;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_tailleEch(int _tailleEch)
    {
        this._tailleEch = _tailleEch;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}