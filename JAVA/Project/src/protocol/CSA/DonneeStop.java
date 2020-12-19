//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 14/12/2020

package protocol.CSA;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeStop implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -7834935748449361391L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _secondes;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeStop()
    {

    }

    public DonneeStop(int _secondes)
    {
        this._secondes = _secondes;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int get_secondes()
    {
        return _secondes;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_secondes(int _secondes)
    {
        this._secondes = _secondes;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return "secondes{=}" + get_secondes() + "#%";
    }

    @Override
    public void setFiledsFromString(String fields)
    {
        String[] row;
        row = fields.split("\\{=}");

        this.set_secondes(Integer.parseInt(row[1]));
    }

}