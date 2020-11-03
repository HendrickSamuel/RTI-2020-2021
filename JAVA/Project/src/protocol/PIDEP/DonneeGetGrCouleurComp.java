//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeGetGrCouleurComp implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -6985593057830045853L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _annee;


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


    /********************************/
    /*            Getters           */
    /********************************/
    public int get_annee()
    {
        return _annee;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_annee(int _annee)
    {
        this._annee = _annee;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}