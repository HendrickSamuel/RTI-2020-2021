//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 28/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.List;

public class DonneeSendLauchPayements implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 337003619695669600L;

    /********************************/
    /*           Variables          */
    /********************************/
    private List<Virement> liste;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeSendLauchPayements()
    {

    }

    public DonneeSendLauchPayements(List<Virement> liste)
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