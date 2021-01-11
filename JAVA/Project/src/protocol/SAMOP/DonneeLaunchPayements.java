//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.List;

public class DonneeLaunchPayements implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 4726880562479364745L;

    /********************************/
    /*           Variables          */
    /********************************/
    private List<Virement> liste;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLaunchPayements()
    {

    }

    public DonneeLaunchPayements(List<Virement> liste)
    {
        this.liste = liste;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public List<Virement> getListe()
    {
        return liste;
    }


    /********************************/
    /*            Setters           */
    /********************************/
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