//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 05/11/2020

package protocol.PIDEP;

import java.io.Serializable;

public class EchANOVA implements Serializable
{
    private static final long serialVersionUID = 8087440585766480376L;

    /********************************/
    /*           Variables          */
    /********************************/
    int jours;
    String ville;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public EchANOVA()
    {

    }

    public EchANOVA(int jours, String ville)
    {
        this.jours = jours;
        this.ville = ville;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getJours()
    {
        return jours;
    }

    public String getVille()
    {
        return ville;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setVille(String ville)
    {
        this.ville = ville;
    }

    public void setJours(int jours)
    {
        this.jours = jours;
    }


    /********************************/
    /*            Methodes          */
    /********************************/

}