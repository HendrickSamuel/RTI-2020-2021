//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 02/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeGetGrCouleurRep implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 4945063507893716496L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _donnee;
    private boolean _annee;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetGrCouleurRep()
    {

    }

    public DonneeGetGrCouleurRep(int _donnee, boolean _annee)
    {
        this._donnee = _donnee;
        this._annee = _annee;
    }


/********************************/
    /*            Getters           */
    /********************************/
    public int get_donnee()
    {
        return _donnee;
    }

    public boolean is_annee()
    {
        return _annee;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_donnee(int _donnee)
    {
        this._donnee = _donnee;
    }

    public void set_annee(boolean _annee)
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