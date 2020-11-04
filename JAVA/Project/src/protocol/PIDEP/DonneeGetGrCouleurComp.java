//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.Vector;

public class DonneeGetGrCouleurComp implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -6985593057830045853L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _annee;

    private Vector _trim1;
    private Vector _trim2;
    private Vector _trim3;
    private Vector _trim4;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetGrCouleurComp()
    {

    }

    public DonneeGetGrCouleurComp(int _annee)
    {
        this._annee = _annee;
    }

    public DonneeGetGrCouleurComp(Vector _trim1, Vector _trim2, Vector _trim3, Vector _trim4)
    {
        this._trim1 = _trim1;
        this._trim2 = _trim2;
        this._trim3 = _trim3;
        this._trim4 = _trim4;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int get_annee()
    {
        return _annee;
    }

    public Vector getTrim1()
    {
        return _trim1;
    }

    public Vector getTrim2()
    {
        return _trim2;
    }

    public Vector getTrim3()
    {
        return _trim3;
    }

    public Vector getTrim4()
    {
        return _trim4;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_annee(int _annee)
    {
        this._annee = _annee;
    }

    public void setTrim1(Vector _trim1)
    {
        this._trim1 = _trim1;
    }

    public void setTrim2(Vector _trim2)
    {
        this._trim2 = _trim2;
    }

    public void setTrim3(Vector _trim3)
    {
        this._trim3 = _trim3;
    }

    public void setTrim4(Vector _trim4)
    {
        this._trim4 = _trim4;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}