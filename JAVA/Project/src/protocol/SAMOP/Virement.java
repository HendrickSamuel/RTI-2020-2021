//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 28/12/2020

package protocol.SAMOP;

import java.io.Serializable;

public class Virement implements Serializable
{
    private static final long serialVersionUID = 6329374608494525186L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int id;
    private String nom;
    private String prenom;
    private Double montant;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Virement()
    {

    }

    public Virement(int id, String nom, String prenom, Double montant)
    {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.montant = montant;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getId()
    {
        return id;
    }

    public String getNom()
    {
        return nom;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public Double getMontant()
    {
        return montant;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setId(int id)
    {
        this.id = id;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    public void setMontant(Double montant)
    {
        this.montant = montant;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return "Virement --> nom : " + nom + " " + prenom + ", montant : " + montant + "€";
    }
}