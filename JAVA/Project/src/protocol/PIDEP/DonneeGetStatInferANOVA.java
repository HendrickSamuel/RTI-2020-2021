//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 03/11/2020

package protocol.PIDEP;

import java.io.Serializable;
import genericRequest.DonneeRequete;

public class DonneeGetStatInferANOVA implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -8005076257115631153L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _tailleEch;

    private double _pvalue;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetStatInferANOVA()
    {

    }

    public DonneeGetStatInferANOVA(int _tailleEch)
    {
        this._tailleEch = _tailleEch;
    }

    public DonneeGetStatInferANOVA(double _pvalue)
    {
        this._pvalue = _pvalue;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public int get_tailleEch()
    {
        return _tailleEch;
    }

    public double get_pvalue()
    {
        return _pvalue;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_tailleEch(int _tailleEch)
    {
        this._tailleEch = _tailleEch;
    }

    public void set_pvalue(double _pvalue)
    {
        this._pvalue = _pvalue;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}