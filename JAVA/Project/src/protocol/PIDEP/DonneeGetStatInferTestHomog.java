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

    private double resultVariance;
    private double varMin;
    private double varMax;

    private double p_value;


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

    public DonneeGetStatInferTestHomog(double resultVariance, double varMin, double varMax, double p_value)
    {
        this.resultVariance = resultVariance;
        this.varMin = varMin;
        this.varMax = varMax;
        this.p_value = p_value;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public int get_tailleEch()
    {
        return _tailleEch;
    }

    public double getResultVariance()
    {
        return resultVariance;
    }

    public double getVarMin()
    {
        return varMin;
    }

    public double getVarMax()
    {
        return varMax;
    }

    public double getP_value()
    {
        return p_value;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_tailleEch(int _tailleEch)
    {
        this._tailleEch = _tailleEch;
    }

    public void setResultVariance(double resultVariance)
    {
        this.resultVariance = resultVariance;
    }

    public void setVarMin(double varMin)
    {
        this.varMin = varMin;
    }

    public void setVarMax(double varMax)
    {
        this.varMax = varMax;
    }

    public void setP_value(double p_value)
    {
        this.p_value = p_value;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}