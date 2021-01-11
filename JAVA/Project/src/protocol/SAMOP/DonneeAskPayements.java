//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.List;

public class DonneeAskPayements implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 7103273795674781873L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int month;

    private List<Virement> liste;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeAskPayements()
    {

    }

    public DonneeAskPayements(List<Virement> liste)
    {
        this.liste = liste;
    }

    public DonneeAskPayements(int month)
    {
        this.month = month;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getMonth()
    {
        return month;
    }

    public List<Virement> getListe()
    {
        return liste;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setMonth(int month)
    {
        this.month = month;
    }

    public void setListe(List<Virement> liste)
    {
        this.liste = liste;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}